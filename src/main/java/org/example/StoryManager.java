package org.example;

import org.example.Comic.*;
import org.example.Completion.CompletionSession;
import org.example.Utils.StringUtil;
import org.example.XML.XMLGenerator;
import org.example.XML.XMLParser;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StoryManager {

    public static void generateRandomStories() {
        String storiesSpec = ConfigurationFile.getValue("STORIES_XML");
        String storiesTarget = ConfigurationFile.getValue("STORIES_TARGET");
        String sourceLang = ConfigurationFile.getValue("SOURCELANGUAGE");
        String targetLang = ConfigurationFile.getValue("TARGETLANGUAGE");

        try {
            String xmlContent = new String(Files.readAllBytes(Paths.get(storiesSpec)));
            Comic storiesInputComic = XMLParser.parseComic(xmlContent);
            Random rand = new Random();
            List<Scene> scenes = new ArrayList<>();

            // Get 10 unique random scenes
            while(scenes.size() < 10) {
                Scene scene = storiesInputComic.getScenes().get(rand.nextInt(storiesInputComic.getScenes().size()));
                if(!scenes.contains(scene)) scenes.add(scene);
            }

            //Generate dialogue for scenes
            List<String> allDialogues = new ArrayList<>();
            List<List<String>> dialogues = new ArrayList<>();
            for(Scene originalScene : scenes) {
                String visualDescription = generateAudiovisualDescriptionForScene(originalScene);
                String speechTemplate = getSpeechForScene(originalScene);
                dialogues = generateDialogueFromDescriptions(visualDescription, speechTemplate);

                for(List<String> panelDialogues : dialogues) {
                    for(String dialogueLine : panelDialogues) {
                        allDialogues.add(StringUtil.removeSpeaker(dialogueLine));
                    }
                }
            }

            //Translate dialogues
            try {
                Translator.batchTranslateList(allDialogues);
            } catch (IOException e) {
                System.err.println("Pre-translation failed: " + e.getMessage());
            }

            Comic finalComic = new Comic();
            finalComic.setFigures(storiesInputComic.getFigures());

            for(Scene originalScene : scenes) {
                // Create bilingual version
                Scene bilingualScene = createBilingualScene(originalScene, dialogues);
                finalComic.addScene(bilingualScene);
            }

            // Generate XML
            XMLGenerator.generateXMLFromComic(finalComic, storiesTarget);

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Scene createBilingualScene(Scene originalScene, List<List<String>> dialogues) {
        Scene newScene = new Scene();

        // Preserve the title panel
        if(!originalScene.getPanels().isEmpty()) {
            newScene.addPanel(originalScene.getPanels().get(0));
        }

        for(int i = 1; i < originalScene.getPanels().size(); i++) {
            Panel originalPanel = originalScene.getPanels().get(i);

            // Create English version
            Panel enPanel = createTranslatedPanel(originalPanel,
                    safeGetDialogues(dialogues, i-1),
                    0);

            // Create target language version
            Panel tgtPanel = createTranslatedPanel(originalPanel,
                    safeGetDialogues(dialogues, i-1),
                    1);

            newScene.addPanel(enPanel);
            newScene.addPanel(tgtPanel);
        }

        return newScene;
    }

    private static List<String> safeGetDialogues(List<List<String>> dialogues, int index) {
        return (index >= 0 && index < dialogues.size())
                ? dialogues.get(index)
                : Collections.emptyList();
    }

    private static Panel createTranslatedPanel(Panel original, List<String> panelDialogues,
                                               int language) {
        Panel newPanel = new Panel();

        // Copy structural elements
        newPanel.setSetting(original.getSetting());
        newPanel.setAbove(original.getAbove());
        newPanel.setBelow(original.getBelow());
        newPanel.setBorder(original.getBorder());

        // Process left side
        if(original.getLeftSide() != null) {
            PanelSide newSide = processPanelSide(original.getLeftSide(),
                    panelDialogues, language);
            newPanel.setLeftSide(newSide);
        }

        //Process middle side
        if(original.getMiddleSide() != null) {
            PanelSide newSide = processPanelSide(original.getMiddleSide(),
                    panelDialogues, language);
            newPanel.setMiddleSide(newSide);
        }

        // Process right side
        if(original.getRightSide() != null) {
            PanelSide newSide = processPanelSide(original.getRightSide(),
                    panelDialogues, language);
            newPanel.setRightSide(newSide);
        }

        return newPanel;
    }

    private static PanelSide processPanelSide(PanelSide original, List<String> panelDialogues,
                                              int language) {
        PanelSide newSide = new PanelSide();
        newSide.setPanelFigure(original.getPanelFigure());

        String characterName = original.getPanelFigure().getName();
        String dialogue = findDialogueForSpeaker(panelDialogues, characterName);

        if(dialogue != null) {
            try {
                String[] translations = Dictionary.getSourceAndTargetTranslations(dialogue);
                String translatedText = dialogue;

                if(translations != null && translations[language] != null) {
                    translatedText = translations[language];
                } else  if (language == 1) {
                    String sourceLang = ConfigurationFile.getValue("SOURCELANGUAGE");
                    String targetLang = ConfigurationFile.getValue("TARGETLANGUAGE");
                    // Fallback to direct translation
                    String fallback = Translator.translateSingleFallback(
                            dialogue, sourceLang, targetLang
                    );
                    if(fallback != null) {
                        translatedText = fallback;
                    }
                }

                newSide.setBallonStatus("speaking");
                newSide.setBalloonContent(translatedText);
            } catch (IOException e) {
                newSide.setBallonStatus("warning");
                newSide.setBalloonContent(dialogue);
            }
        }

        return newSide;
    }

    // Update the prompt in generateDialogueFromDescriptions
    private static List<List<String>> generateDialogueFromDescriptions(String input, String format) {
        String messageContent = "Generate natural character dialogue in this exact format:\n"
                + "1. [Character1]: \"[Dialogue1]\" / [Character2]: \"[Dialogue2]\"\n"
                + "2. [Character1]: \"[Dialogue3]\"\n"
                + "...\n"
                + "Based on this scene description:\n"
                + input + "\n"
                + "Template:\n" + format;

        CompletionSession session = new CompletionSession();
        String response = session.sendMessage("user", messageContent);

        return MessageParser.parseNumberedDialogue(response);
    }

    private static String findDialogueForSpeaker(List<String> panelDialogues, String speakerName) {
        if(speakerName == null) return null;

        for(String dialogue : panelDialogues) {
            if(dialogue.startsWith(speakerName + ":")) {
                return StringUtil.removeSpeaker(dialogue);
            }
        }
        return null;
    }

    private static String getSpeechForScene(Scene scene) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < scene.getPanels().size(); i++) {
            Panel panel = scene.getPanels().get(i);
            sb.append(i + ". " + getSpeechForPanel(panel) + "\n");
        }
        return sb.toString();
    }

    private static String getSpeechForPanel(Panel panel) {
        StringBuilder sb = new StringBuilder();
        if(panelSideHasCharacter(panel.getLeftSide())) {
            String name = panel.getLeftSide().getPanelFigure().getName();
            if(name != null) sb.append(" " + name + ": ___");
        }
        if(panelSideHasCharacter(panel.getRightSide())) {
            String name = panel.getRightSide().getPanelFigure().getName();
            if(name != null) sb.append(" " + name + ": ___");
        }
        if(panelSideHasCharacter(panel.getMiddleSide())) {
            String name = panel.getMiddleSide().getPanelFigure().getName();
            if(name != null) sb.append(" " + name + ": ___");
        }
        return sb.toString();
    }

    private static String generateAudiovisualDescriptionForScene(Scene scene) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < scene.getPanels().size(); i++) {
            Panel panel = scene.getPanels().get(i);
            sb.append(i + ". " + getAudiovisualDescriptionForPanel(panel) + "\n");
        }
        return sb.toString();
    }

    private static String getAudiovisualDescriptionForPanel(Panel panel) {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + getSetting(panel) + ")");
        if(panelSideHasCharacter(panel.getLeftSide())) sb.append(" On the left " + getAudiovisualDescriptionForPanelSide(panel.getLeftSide()));
        if(panelSideHasCharacter(panel.getMiddleSide())) sb.append(" In the middle " + getAudiovisualDescriptionForPanelSide(panel.getMiddleSide()));
        if(panelSideHasCharacter(panel.getRightSide())) sb.append(" On the right " + getAudiovisualDescriptionForPanelSide(panel.getRightSide()));
        if(panel.getBelow() != null) sb.append(" " + panel.getBelow() + ".");
        return sb.toString();
    }

    private static String getAudiovisualDescriptionForPanelSide(PanelSide panelSide) {
        if(panelSide.getPanelFigure() != null && panelSide.getPanelFigure().getName() != null) {
            return panelSide.getPanelFigure().getName() + " is " + panelSide.getBalloonContent() + ".";
        }
        return "";
    }

    private static String getSetting(Panel panel) {
        if(panel.getAbove() != null) return panel.getAbove();
        return panel.getSetting();
    }

    private static boolean panelSideHasCharacter(PanelSide panelSide) {
        return panelSide != null && panelSide.getPanelFigure() != null && panelSide.getPanelFigure().getName() != null;
    }

}

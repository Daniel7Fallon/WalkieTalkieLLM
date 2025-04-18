package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Comic.Dialogue.PanelDialogues;
import org.example.Comic.Dialogue.SceneDialogue;
import org.example.Translation.Dictionary;
import org.example.Translation.Translator;
import org.example.Utils.ConfigurationFile;
import org.example.Comic.Dialogue.DialogueManager;

public class ComicPostProcessor {

    /* Takes a comic that has it's dialogues already translated
     * Returns new comic with translated panels interleaved
     */
    public static Comic addTranslationPanels(Comic originalComic) {
        Comic newComic = new Comic();
        List<Scene> newScenes = new ArrayList<>();
        for (Scene scene : originalComic.getScenes()) {

            List<Panel> newPanels = new ArrayList<>();
            for (Panel originalPanel : scene.getPanels()) {
                newPanels.add(originalPanel.deepCopy());

                // Create translated version if translatable
                if (originalPanel.hasBalloonContent()) {
                    try {
                        Panel translated = createTranslatedPanel(originalPanel);
                        if (translated != null) {
                            newPanels.add(translated);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            Scene newScene = new Scene();
            newScene.addAllPanels(newPanels);
            newScenes.add(newScene);
        }
        newComic.addAllScenes(newScenes);
        newComic.removePluralIdentifiers();
        return newComic;
    }

    private static Panel createTranslatedPanel(Panel original) {
        Panel translated = new Panel();
        translated.setSetting(original.getSetting());
        translated.setBorder(original.getBorder());
        translated.setBelow(original.getBelow());

        // Map original side to translated sides
        if (original.hasLeft()) {
            translated.setLeftSide(createTranslatedSide(original.getLeftSide()));
        }

        if (original.hasMiddle()) {
            translated.setMiddleSide(createTranslatedSide(original.getMiddleSide()));
        }

        if (original.hasRight()) {
            translated.setRightSide(createTranslatedSide(original.getRightSide()));
        }

        return translated;
    }

    private static PanelSide createTranslatedSide(PanelSide originalPanelSide) {
        PanelSide translatedPanelSide = new PanelSide();
        translatedPanelSide.setPanelFigure(originalPanelSide.getPanelFigure());

        if (originalPanelSide.hasBalloonContent()) {
            try {
                String[] translations = Dictionary.getSourceAndTargetTranslations(
                        originalPanelSide.getBalloonContent()
                );
                translatedPanelSide.setBalloonContent(translations[1]);
                translatedPanelSide.setBalloonStatus("speaking");
            } catch (IOException | NullPointerException e) {
                translatedPanelSide.setBalloonContent("[TRANSLATION FAILED]");
                translatedPanelSide.setBalloonStatus("error");
            }
        }
        return translatedPanelSide;
    }

    public static Scene createBilingualScene(Scene originalScene, SceneDialogue sceneDialogue) {
        Scene newScene = new Scene();

        // Preserve the title panel
        if(!originalScene.getPanels().isEmpty()) {
            newScene.addPanel(originalScene.getPanels().get(0));
        }

        for(int i = 1; i < originalScene.getPanels().size(); i++) {
            Panel originalPanel = originalScene.getPanels().get(i);
            PanelDialogues panelDialogues = sceneDialogue.getPanelDialogues().get(i-1);

            // Create English version
            Panel enPanel = createTranslatedPanel(originalPanel,
                    panelDialogues,
                    0);

            // Create target language version
            Panel tgtPanel = createTranslatedPanel(originalPanel,
                    panelDialogues,
                    1);

            newScene.addPanel(enPanel);
            newScene.addPanel(tgtPanel);
        }

        return newScene;
    }

    private static Panel createTranslatedPanel(Panel original, PanelDialogues panelDialogues,
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
                    panelDialogues.getDialogues(), language);
            newPanel.setLeftSide(newSide);
        }

        //Process middle side
        if(original.getMiddleSide() != null) {
            PanelSide newSide = processPanelSide(original.getMiddleSide(),
                    panelDialogues.getDialogues(), language);
            newPanel.setMiddleSide(newSide);
        }

        // Process right side
        if(original.getRightSide() != null) {
            PanelSide newSide = processPanelSide(original.getRightSide(),
                    panelDialogues.getDialogues(), language);
            newPanel.setRightSide(newSide);
        }

        return newPanel;
    }

    private static PanelSide processPanelSide(PanelSide original, List<String> panelDialogues,
                                              int language) {
        PanelSide newSide = new PanelSide();
        newSide.setPanelFigure(original.getPanelFigure());

        String characterName = original.getPanelFigure().getName();
        String dialogue = DialogueManager.findDialogueForSpeaker(panelDialogues, characterName);

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

                newSide.setBalloonStatus("speaking");
                newSide.setBalloonContent(translatedText);
            } catch (IOException e) {
                newSide.setBalloonStatus("warning");
                newSide.setBalloonContent(dialogue);
            }
        }

        return newSide;
    }
}
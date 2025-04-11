package org.example;

import org.example.Comic.*;
import org.example.Completion.CompletionSession;
import org.example.Utils.StringUtil;
import org.example.XML.XMLParser;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryManager {

    public static void generateRandomStories() {
        String storiesSpec = ConfigurationFile.getValue("STORIES_XML");
        String storiesTarget = ConfigurationFile.getValue("STORIES_TARGET");

        try {
            String xmlContent = new String(Files.readAllBytes(Paths.get(storiesSpec)));
            Comic storiesInputComic = XMLParser.parseComic(xmlContent);
            Random rand = new Random();
            List<Scene> scenes = new ArrayList<>();
            while(scenes.size() < 10) {
                Scene scene = storiesInputComic.getScenes().get(rand.nextInt(storiesInputComic.getScenes().size()));
                if(!scenes.contains(scene)) scenes.add(scene);
            }

            for(Scene scene : scenes) {
                System.out.println(generateAudiovisualDescriptionForScene(scene));
                System.out.println(getSpeechForScene(scene));

                String input = generateAudiovisualDescriptionForScene(scene);
                String format = getSpeechForScene(scene);

                // List of dialogues
                List<List<String>> dialogues = generateDialogueFromDescriptions(input, format);

                // Example usage
                for (int i = 0; i < dialogues.size(); i++) {
                    System.out.println(i + ".");
                    for (int j = 0; j < dialogues.get(i).size(); j++) {
                        System.out.println(StringUtil.removeSpeaker(dialogues.get(i).get(j)));
                    }
                }

                // When translating, use StringUtil.removeSpeaker to get rid of the speaker from the dialogue.
            }

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
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

    private static List<List<String>> generateDialogueFromDescriptions(String input, String format) {
        String messageContent = "I am going to give you a numbered list, where each number corresponds to a panel in a scene of a comic."
                + input
                + "Please generate dialogue based off of that numbered list such that the gaps in the numbered list below "
                + "are filled. Please do not generate or say anything other than the numbered list."
                + format;

        CompletionSession session = new CompletionSession();
        String response = session.sendMessage("user", messageContent);

        return MessageParser.parseNumberedDialogue(response);
    }

}

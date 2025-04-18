package org.example.Comic.Dialogue;

import org.example.Comic.Panel;
import org.example.Comic.PanelSide;
import org.example.Comic.Scene;
import org.example.Completion.CompletionSession;
import org.example.Utils.MessageParser;

import java.util.ArrayList;
import java.util.List;

public class DialogueManager {

    public static List<SceneDialogue> generateDialogueForScenes(List<Scene> originalScenes) {
        List<SceneDialogue> sceneDialogues = new ArrayList<>();

        for(Scene originalScene : originalScenes) {
            String visualDescription = generateAudiovisualDescriptionForScene(originalScene);
            String speechTemplate = getSpeechForScene(originalScene);
            sceneDialogues.add(generateSceneDialogueFromDescriptions(visualDescription, speechTemplate));
        }

        return sceneDialogues;
    }

    private static SceneDialogue generateSceneDialogueFromDescriptions(String input, String format) {
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
        if(panel.hasLeft() && panel.getLeftSide().hasCharacter()) {
            String name = panel.getLeftSide().getPanelFigure().getName();
            if(name != null) sb.append(" " + name + ": ___");
        }
        if(panel.hasMiddle() && panel.getMiddleSide().hasCharacter()) {
            String name = panel.getMiddleSide().getPanelFigure().getName();
            if(name != null) sb.append(" " + name + ": ___");
        }
        if(panel.hasRight() && panel.getRightSide().hasCharacter()) {
            String name = panel.getRightSide().getPanelFigure().getName();
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
        sb.append("(" + getAudioVisualSetting(panel) + ")");
        if(panel.hasLeft() && panel.getLeftSide().hasCharacter()) sb.append(" On the left " + getAudiovisualDescriptionForPanelSide(panel.getLeftSide()));
        if(panel.hasMiddle() && panel.getMiddleSide().hasCharacter()) sb.append(" In the middle " + getAudiovisualDescriptionForPanelSide(panel.getMiddleSide()));
        if(panel.hasRight() && panel.getRightSide().hasCharacter()) sb.append(" On the right " + getAudiovisualDescriptionForPanelSide(panel.getRightSide()));
        if(panel.getBelow() != null) sb.append(" " + panel.getBelow() + ".");
        return sb.toString();
    }

    private static String getAudiovisualDescriptionForPanelSide(PanelSide panelSide) {
        if(panelSide.getPanelFigure() != null && panelSide.getPanelFigure().getName() != null) {
            return panelSide.getPanelFigure().getName() + " is " + panelSide.getBalloonContent() + ".";
        }
        return "";
    }

    private static String getAudioVisualSetting(Panel panel) {
        if(panel.getAbove() != null) return panel.getAbove();
        return panel.getSetting();
    }
}

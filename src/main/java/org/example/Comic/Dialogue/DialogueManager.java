package org.example.Comic.Dialogue;

import org.example.Comic.Panel;
import org.example.Comic.PanelSide;
import org.example.Comic.Scene;
import org.example.Completion.CompletionSession;
import org.example.Utils.MessageParser;
import org.example.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogueManager {

    public static class DialogueGenerationResult {
        private final List<SceneDialogue> sceneDialogues;
        private final List<String> allDialogueLines;

        DialogueGenerationResult(List<SceneDialogue> sceneDialogues, List<String> allDialogueLines) {
            this.sceneDialogues = sceneDialogues;
            this.allDialogueLines = allDialogueLines;
        }

        public List<SceneDialogue> getSceneDialogues() {
            return sceneDialogues;
        }

        public List<String> getAllDialogueLines() {
            return allDialogueLines;
        }
    }

    public static DialogueGenerationResult generateDialogueForScenes(List<Scene> originalScenes) {
        List<String> allDialogues = new ArrayList<>();
        List<SceneDialogue> sceneDialogues = new ArrayList<>();

        for(Scene originalScene : originalScenes) {
            String visualDescription = generateAudiovisualDescriptionForScene(originalScene);
            String speechTemplate = getSpeechForScene(originalScene);
            sceneDialogues.add(generateDialogueFromDescriptions(visualDescription, speechTemplate));

            for(SceneDialogue sceneDialogue: sceneDialogues) {
                for (PanelDialogues panelDialogues : sceneDialogue.getPanelDialogues()) {
                    for (String dialogueLine : panelDialogues.getDialogues()) {
                        allDialogues.add(StringUtil.removeSpeaker(dialogueLine));
                    }
                }
            }
        }

        System.out.println(sceneDialogues);
        System.out.println(allDialogues);

        return new DialogueGenerationResult(sceneDialogues, allDialogues);
    }

    private static SceneDialogue generateDialogueFromDescriptions(String input, String format) {
        String messageContent = "Generate natural character dialogue in this exact format:\n"
                + "1. [Character1]: \"[Dialogue1]\" / [Character2]: \"[Dialogue2]\"\n"
                + "2. [Character1]: \"[Dialogue3]\"\n"
                + "...\n"
                + "Based on this scene description:\n"
                + input + "\n"
                + "Template:\n" + format;

        CompletionSession session = new CompletionSession();
        String response = session.sendMessage("user", messageContent);

        SceneDialogue sceneDialogue = new SceneDialogue();

        List<List<String>> parsedScene = MessageParser.parseNumberedDialogue(response);

        for(List<String> pd : parsedScene) {
            sceneDialogue.addPanelDialogues(new PanelDialogues(pd));
        }
        return sceneDialogue;
    }

    public static String findDialogueForSpeaker(List<String> panelDialogues, String speakerName) {
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

    private static boolean panelSideHasCharacter(PanelSide panelSide) {
        return panelSide != null && panelSide.getPanelFigure() != null && panelSide.getPanelFigure().getName() != null;
    }

    private static String getSetting(Panel panel) {
        if(panel.getAbove() != null) return panel.getAbove();
        return panel.getSetting();
    }
}

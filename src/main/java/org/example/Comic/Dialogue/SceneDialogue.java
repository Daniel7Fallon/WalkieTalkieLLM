package org.example.Comic.Dialogue;

import org.example.Completion.CompletionSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SceneDialogue {
    List<PanelDialogue> panelDialogues = new ArrayList<>();

    public List<PanelDialogue> getPanelDialogues() {
        return panelDialogues;
    }
    public void setPanelDialogues(List<PanelDialogue> panelDialogues) {
        this.panelDialogues = panelDialogues;
    }
    public void addPanelDialogue(PanelDialogue panelDialogues) {
        this.panelDialogues.add(panelDialogues);
    }

    public static SceneDialogue generateSceneDialogueFromDescriptions(String input, String format) {
        String messageContent = "Generate natural character dialogue in this exact format:\n"
                + "1. [Character1]: \"[Dialogue1]\" / [Character2]: \"[Dialogue2]\"\n"
                + "2. [Character1]: \"[Dialogue3]\"\n"
                + "...\n"
                + "Based on this scene description:\n"
                + input + "\n"
                + "Template:\n" + format;

        CompletionSession session = new CompletionSession();
        String response = session.sendMessage("user", messageContent);

        return parseNumberedDialogue(response);
    }

    private static SceneDialogue parseNumberedDialogue(String input) {
        Pattern panelPattern = Pattern.compile("^(\\d+)\\.\\s*(.*)$");
        Pattern dialoguePattern = Pattern.compile("(\\w+):\\s*\"([^\"]*)\"");

        SceneDialogue sceneDialogue = new SceneDialogue();

        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            Matcher panelMatcher = panelPattern.matcher(line);
            if (panelMatcher.matches()) {
                String content = panelMatcher.group(2);
                Matcher dialogueMatcher = dialoguePattern.matcher(content);

                PanelDialogue panelDialogue = new PanelDialogue();
                while (dialogueMatcher.find()) {
                    String speaker = dialogueMatcher.group(1);
                    String text = dialogueMatcher.group(2);
                    CharacterDialogue characterDialogue = new CharacterDialogue(speaker, text);
                    panelDialogue.addCharacterDialogue(characterDialogue);
                }
                sceneDialogue.addPanelDialogue(panelDialogue);
            }
        }

        return sceneDialogue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Scene Dialogue:\n");
        for (PanelDialogue panelDialogue : panelDialogues) {
            sb.append(panelDialogue.toString());
        }
        return sb.toString();
    }

}

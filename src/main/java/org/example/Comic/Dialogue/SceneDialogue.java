package org.example.Comic.Dialogue;

import org.example.Completion.CompletionSession;
import org.example.Utils.MessageParser;

import java.util.ArrayList;
import java.util.List;

public class SceneDialogue {
    List<PanelDialogue> panelDialogues = new ArrayList<PanelDialogue>();

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

        return MessageParser.parseNumberedDialogue(response);
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

package org.example.Comic.Dialogue;

import java.util.ArrayList;
import java.util.List;

public class PanelDialogues {
    private List<String> dialogues = new ArrayList<String>();
    public PanelDialogues(List<String> dialogues) {
        this.dialogues = dialogues;
    }
    public List<String> getDialogues() {
        return dialogues;
    }
    public void setDialogues(List<String> dialogues) {
        this.dialogues = dialogues;
    }
}
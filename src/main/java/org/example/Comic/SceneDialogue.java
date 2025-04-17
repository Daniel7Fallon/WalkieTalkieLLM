package org.example.Comic;

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
    public void addPanelDialogues(PanelDialogue panelDialogue) {
        this.panelDialogues.add(panelDialogue);
    }

    public static class PanelDialogue{
        private List<String> dialogues = new ArrayList<String>();
        public PanelDialogue(List<String> dialogues) {
            this.dialogues = dialogues;
        }
        public List<String> getDialogues() {
            return dialogues;
        }
        public void setDialogues(List<String> dialogues) {
            this.dialogues = dialogues;
        }
    }
}

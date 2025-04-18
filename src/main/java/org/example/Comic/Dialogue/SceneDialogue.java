package org.example.Comic.Dialogue;

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

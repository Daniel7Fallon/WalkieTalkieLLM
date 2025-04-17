package org.example.Comic.Dialogue;

import java.util.ArrayList;
import java.util.List;

public class SceneDialogue {
    List<PanelDialogues> panelDialogues = new ArrayList<PanelDialogues>();

    public List<PanelDialogues> getPanelDialogues() {
        return panelDialogues;
    }
    public void setPanelDialogues(List<PanelDialogues> panelDialogues) {
        this.panelDialogues = panelDialogues;
    }
    public void addPanelDialogues(PanelDialogues panelDialogues) {
        this.panelDialogues.add(panelDialogues);
    }


}

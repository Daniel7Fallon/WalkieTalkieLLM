package org.example.Comic;

import java.util.ArrayList;
import java.util.List;

import org.example.Comic.Dialogue.SceneDialogue;
import org.example.Comic.Panel;

public class Scene {
    private List<Panel> panels = new ArrayList<>();

    public void addPanel(Panel panel) {
        panels.add(panel);
    }
    public List<Panel> getPanels() {
        return panels;
    }
    public void addAllPanels(List<Panel> panels) {
        this.panels.addAll(panels);
    }

    public Panel getPanelByIndex(int index) {
       return panels.get(index);
    }

    public void addPanelAtIndex(int index, Panel panel) {
        panels.add(index, panel);
    }

    public void skipTitleReplaceDialogue(SceneDialogue sceneDialogue) {
        if(sceneDialogue.getPanelDialogues().size() +1 != panels.size()) throw new IllegalArgumentException("SceneDialogue size +1 does not equal panels size.");
        for(int i = 1; i < panels.size(); i++) {
            Panel currentPanel = panels.get(i);
            currentPanel.replaceDialogue(sceneDialogue.getPanelDialogues().get(i-1));
        }
    }

    public Scene deepCopy() {
        Scene newScene = new Scene();
        for (Panel panel : this.panels) {
            newScene.addPanel(panel.deepCopy());
        }
        return newScene;
    }

    public void splitMultiDialoguePanels() {
        int i = 0;
        while (i < panels.size()) {
            Panel panel = panels.get(i);
            if(panel.hasLeft() && panel.hasRight() && panel.getLeftSide().getBalloonStatus() != null && panel.getRightSide().getBalloonStatus() != null) {
                Panel newPanel = panel.deepCopy();


                if(!newPanel.hasLeft()) System.out.println("new panel has no left side");
                if(!newPanel.hasRight()) System.out.println("new panel has no right side");

                panel.getRightSide().setBalloonContent(null);
                panel.getRightSide().setBalloonStatus(null);
                newPanel.getLeftSide().setBalloonContent(null);
                newPanel.getLeftSide().setBalloonStatus(null);
                this.addPanelAtIndex(i+1, newPanel);
            }
            i++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Panel panel : panels) {
            sb.append(panel);
        }
        return sb.toString();
    }
}

package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Comic.Dialogue.PanelDialogue;
import org.example.Comic.Dialogue.SceneDialogue;

public class Scene {
    private final List<Panel> panels = new ArrayList<>();

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

    //Returns true if successful
    public boolean removeFirstPanel() {
        if(panels.isEmpty()) return false;
        panels.removeFirst();
        return true;
    }

    /**
     * Generates a formatted string representing the speech template for panels in this scene.
     *
     * @return A multi-line string containing numbered speech templates for each panel.
     */
    public String getSpeechTemplate() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < this.getPanels().size(); i++) {
            Panel panel = this.getPanelByIndex(i);
            sb.append(i + ". " + panel.getSpeechTemplate() + "\n");
        }
        return sb.toString();
    }

    /**
     * Generates a formatted string representing the audiovisual description for panels in this scene.
     * @return A multi-line string containing numbered audiovisual descriptions for each panel.
     */
    public String generateAudiovisualDescription() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < this.getPanels().size(); i++) {
            Panel panel = this.getPanels().get(i);
            sb.append(i + ". " + panel.getAudiovisualDescription() + "\n");
        }
        return sb.toString();
    }

    /**
     * Replaces the dialogue in panels of this scene (skipping the first panel)
     * with dialogue from the provided SceneDialogue object.
     * @param sceneDialogue The SceneDialogue containing PanelDialogues to insert.
     * @throws IllegalArgumentException if the number of PanelDialogues + 1 does not match the number of panels in the scene.
     */
    public void skipTitleReplaceDialogue(SceneDialogue sceneDialogue) {
        if(sceneDialogue.getPanelDialogues().size() +1 != panels.size()) {

            System.out.println("Panel Dialogues: ");
            for(PanelDialogue panelDialogue : sceneDialogue.getPanelDialogues()) {
                System.out.println(panelDialogue);
            }
            System.out.println("Panel Dialogues size: " + sceneDialogue.getPanelDialogues().size());
            System.out.println("Panels size: " + panels.size());

            throw new IllegalArgumentException("SceneDialogue size +1 does not equal panels size.");
        }
        for(int i = 1; i < panels.size(); i++) {
            Panel currentPanel = panels.get(i);
            currentPanel.replaceDialogue(sceneDialogue.getPanelDialogues().get(i-1));
        }
    }

    // Creates a deep copy of this scene.
    public Scene deepCopy() {
        Scene newScene = new Scene();
        for (Panel panel : this.panels) {
            newScene.addPanel(panel.deepCopy());
        }
        return newScene;
    }

    // Splits panels with multiple dialogues into separate panels. This is done in place.
    public void splitMultiDialoguePanels() {
        int i = 0;
        while (i < panels.size()) {
            Panel panel = panels.get(i);
            if(panel.hasLeft() && panel.hasRight() && panel.getLeftSide().getBalloonStatus() != null && panel.getRightSide().getBalloonStatus() != null) {
                Panel newPanel = panel.deepCopy();
                panel.getRightSide().setBalloonContent(null);
                panel.getRightSide().setBalloonStatus(null);
                newPanel.getLeftSide().setBalloonContent(null);
                newPanel.getLeftSide().setBalloonStatus(null);
                this.addPanelAtIndex(i+1, newPanel);
            }
            i++;
        }
    }

    /**
     * Adds audio references to all panels within this scene.
     * Delegates the actual audio generation/linking to the Panel objects.
     * @throws IOException if audio file operations fail.
     * @throws InterruptedException if audio generation is interrupted (from AudioManager.createNewAudio()).
     */
    public void addAudio() throws IOException, InterruptedException {
        for(Panel panel : getPanels()) {
            panel.addAudio();
        }
    }

    // Removes "Above" and "Below" elements from all panels in this scene.
    public void removeAllAboveAndBelow() {
        for(Panel panel : panels) {
            panel.setAbove(null);
            panel.setBelow(null);
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

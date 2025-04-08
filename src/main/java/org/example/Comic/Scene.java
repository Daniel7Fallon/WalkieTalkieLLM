package org.example.Comic;

import java.util.ArrayList;
import java.util.List;

import org.example.Comic.Panel;

public class Scene {
    private List<Panel> panels = new ArrayList<>();

    public void addPanel(Panel panel) {
        panels.add(panel);
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public Panel getPanelByIndex(int index) {
       return panels.get(index);
    }

    public void addPanelAtIndex(int index, Panel panel) {
        panels.add(index, panel);
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

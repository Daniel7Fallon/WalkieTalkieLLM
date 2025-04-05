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
}

package org.example.Comic;

import java.util.ArrayList;
import java.util.List;

public class Comic {
    private List<Figure> figures;
    private List<Scene> scenes;

    public Comic() {
        figures = new ArrayList<>();
        scenes = new ArrayList<>();
    }

    public Figure getFigureById(String id) {
        for (Figure figure : figures) {
            if(figure.getId().equals(id)) return figure;
        }
        return null;
    }

    public List<String> getAllBubbleContent() {
        List<String> allBubbleContent = new ArrayList<>();
        for(Scene scene : scenes) {
            for(Panel panel : scene.getPanels()) {
                if(panel.getLeftSide() != null && panel.getLeftSide().getBalloonContent() != null) allBubbleContent.add(panel.getLeftSide().getBalloonContent());
                if(panel.getMiddleSide() != null && panel.getMiddleSide().getBalloonContent() != null) allBubbleContent.add(panel.getMiddleSide().getBalloonContent());
                if(panel.getRightSide() != null && panel.getRightSide().getBalloonContent() != null) allBubbleContent.add(panel.getRightSide().getBalloonContent());
            }
        }
        return allBubbleContent;
    }

    public List<Figure> getFigures() {
        return figures;
    }
    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }
    public void addFigure(Figure figure) {
        this.figures.add(figure);
    }
    public void addAllFigures(List<Figure> figures) {
        this.figures.addAll(figures);
    }

    public List<Scene> getScenes() {
        return scenes;
    }
    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }
    public void addScene(Scene scene) {
        this.scenes.add(scene);
    }
    public void addAllScenes(List<Scene> scenes) {
        this.scenes.addAll(scenes);
    }
}

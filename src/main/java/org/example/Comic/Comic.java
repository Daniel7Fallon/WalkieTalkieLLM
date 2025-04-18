package org.example.Comic;

import org.example.Comic.Dialogue.SceneDialogue;

import java.util.ArrayList;
import java.util.List;

import static org.example.Comic.Dialogue.SceneDialogue.generateSceneDialogueFromDescriptions;
import static org.example.Utils.StringUtil.removePluralIdentifier;

public class Comic {
    private List<Figure> figures;
    private List<Scene> scenes;

    public Comic() {
        figures = new ArrayList<>();
        scenes = new ArrayList<>();
    }

    /* Takes a list of SceneDialogues
     * Copies comic and inserts new dialogue
     * Skips title card
     */
    public void replaceDialogue(List<SceneDialogue> sceneDialogues) {
        if(sceneDialogues.size() != scenes.size()) {
            throw new IllegalArgumentException("SceneDialogues size does not match scene size in comic");
        }
        for(int i = 0; i < this.getScenes().size(); i++) {
            SceneDialogue sceneDialogue = sceneDialogues.get(i);
            Scene currentScene = this.getScenes().get(i);
            currentScene.skipTitleReplaceDialogue(sceneDialogue);
        }
    }

    public Comic deepCopy() {
        Comic newComic = new Comic();
        newComic.figures = figures;
        for(Scene scene : scenes) {
            newComic.addScene(scene.deepCopy());
        }
        return newComic;
    }

    public Figure getFigureById(String id) {
        for (Figure figure : figures) {
            if(figure.getId().equals(id)) return figure;
        }
        return null;
    }


    // Returns a list of scene dialogues generated from a comic containing audiovisual descriptions
    public List<SceneDialogue> generateDialogueFromAudioDescriptionComic() {
        List<SceneDialogue> sceneDialogues = new ArrayList<>();

        for(Scene originalScene : this.scenes) {
            String visualDescription = originalScene.generateAudiovisualDescription();
            String speechTemplate = originalScene.getSpeechTemplate();
            sceneDialogues.add(generateSceneDialogueFromDescriptions(visualDescription, speechTemplate));
        }

        return sceneDialogues;
    }

    public List<String> getAllBalloonContent() {
        List<String> balloonContents = new ArrayList<>();
        for(Scene scene: this.getScenes()) {
            for(Panel panel : scene.getPanels()) {
                if(panel.hasLeft() && panel.getLeftSide().hasBalloonContent()) balloonContents.add(panel.getLeftSide().getBalloonContent());
                if(panel.hasMiddle() && panel.getMiddleSide().hasBalloonContent()) balloonContents.add(panel.getMiddleSide().getBalloonContent());
                if(panel.hasRight() && panel.getRightSide().hasBalloonContent()) balloonContents.add(panel.getRightSide().getBalloonContent());
            }
        }
        return balloonContents;
    }

    public void removePluralIdentifiers() {
        for(Scene scene : scenes) {
            for(Panel panel : scene.getPanels()) {
                PanelSide left = panel.getLeftSide();
                PanelSide middle = panel.getMiddleSide();
                PanelSide right = panel.getRightSide();

                if(left != null && left.hasBalloonContent()) left.setBalloonContent(removePluralIdentifier(left.getBalloonContent()));
                if(middle != null && middle.hasBalloonContent()) middle.setBalloonContent(removePluralIdentifier(middle.getBalloonContent()));
                if(right != null && right.hasBalloonContent()) right.setBalloonContent(removePluralIdentifier(right.getBalloonContent()));
            }
        }
    }

    public void splitAllMultiDialoguePanels() {
        for(Scene scene: this.getScenes()) {
            scene.splitMultiDialoguePanels();
        }
    }

    public void removeAllAboveAndBelow() {
        for(Scene scene: this.getScenes()) {
            scene.removeAllAboveAndBelow();
        }
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

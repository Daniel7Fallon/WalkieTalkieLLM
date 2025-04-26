package org.example.Comic;

import org.example.Comic.Dialogue.SceneDialogue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.Comic.Dialogue.SceneDialogue.generateSceneDialogueFromDescriptions;
import static org.example.Utils.StringUtil.removePluralIdentifier;

public class Comic {
    static final Random RAND = new Random();
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

    //Returns true if successful
    public boolean removeFirstPanel() {
        if(getScenes().isEmpty()) return false;
        return getScenes().getFirst().removeFirstPanel();
    }

    public void addSectionPanel(int n, String sectionType) {
        Panel sectionPanel = new Panel();
        sectionPanel.setBelow("Section " + n + ": " + sectionType);
        sectionPanel.setBorder("white");
        PanelSide middle = new PanelSide();
        PanelFigure panelFigure = new PanelFigure(figures.getFirst());
        panelFigure.setFacing("right");
        middle.setPanelFigure(panelFigure);
        sectionPanel.setMiddleSide(middle);

        scenes.getFirst().addPanelAtIndex(0, sectionPanel);
    }

    public void appendComic(Comic that) {
        this.addAllFigures(that.getFigures());
        this.addAllScenes(that.getScenes());
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

    public void addAudio() throws IOException, InterruptedException {
        for(Scene scene : getScenes()) {
            scene.addAudio();
        }
    }

    public void removeAllAboveAndBelow() {
        for(Scene scene: this.getScenes()) {
            scene.removeAllAboveAndBelow();
        }
    }

    public List<Scene> getRandomScenes(int numOfScenes) {
        List<Scene> scenes = new ArrayList<>();
        for (int i = 0; i < numOfScenes; i++) {
            int randomIndex = RAND.nextInt(this.getScenes().size());
            Scene scene = this.getScenes().get(randomIndex);
            if(!scenes.contains(scene)) scenes.add(scene);
        }
        return scenes;
    }

    public List<Figure> getFigures() {
        return figures;
    }
    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }
    //Skips existing ids or names
    public void addFigure(Figure figure) {
        for(Figure f : figures) {
            if(f.getId().equals(figure.getId()) || f.getName().equals(figure.getName())) return;
        }
        this.figures.add(figure);
    }
    //Skips existing ids or names
    public void addAllFigures(List<Figure> figures) {
        for(Figure f : figures) {
            this.addFigure(f);
        }
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

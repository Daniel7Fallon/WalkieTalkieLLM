package org.example.Orchestration;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;

import org.jdom2.JDOMException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Orchestrator {

    /* Takes numberOfStories scenes from stories spec
     * Generates dialogue for those scenes
     * Returns a comic with those scenes with AI dialogue
     */

    /*
        Move all the comic stuff from main here, turn them into methods.
        Feel free to not use the current framework and change anything as needed.
     */

    public static Comic generateRandomStoriesComic(Comic originalComic, int numberOfStories) throws IOException, JDOMException {
        List<Scene> scenes = originalComic.getRandomScenes(numberOfStories);

        Comic finalComic = new Comic();
        finalComic.setFigures(originalComic.getFigures());
        finalComic.addAllScenes(scenes);

        List<SceneDialogue> sceneDialogues = finalComic.generateDialogueFromAudioDescriptionComic();

        finalComic.removeAllAboveAndBelow();
        finalComic.replaceDialogue(sceneDialogues);

        return finalComic;
    }

    public static Comic generateVignetteComic(int num) {
        Comic comic = new Comic();

        // TODO

        return comic;
    }

    public static Comic generateConjugationComic(int num) {
        Comic comic = new Comic();

        // TODO

        return comic;
    }




}

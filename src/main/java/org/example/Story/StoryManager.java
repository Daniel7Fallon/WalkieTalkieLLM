package org.example.Story;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;

import org.jdom2.JDOMException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryManager {
    static final Random RAND = new Random();

    /* Takes numberOfStories scenes from stories spec
     * Generates dialogue for those scenes
     * Returns a comic with those scenes with AI dialogue
     */
    public static Comic generateRandomStoriesComic(Comic originalComic, int numberOfStories) throws IOException, JDOMException {
        List<Scene> scenes = getRandomScenesFromComic(originalComic, numberOfStories);

        Comic finalComic = new Comic();
        finalComic.setFigures(originalComic.getFigures());
        finalComic.addAllScenes(scenes);

        List<SceneDialogue> sceneDialogues = finalComic.generateDialogueFromAudioDescriptionComic();

        finalComic.removeAllAboveAndBelow();
        finalComic.replaceDialogue(sceneDialogues);

        return finalComic;
    }

    private static List<Scene> getRandomScenesFromComic(Comic comic, int numOfScenes) {
        List<Scene> scenes = new ArrayList<>();
        for (int i = 0; i < numOfScenes; i++) {
            int randomIndex = RAND.nextInt(comic.getScenes().size());
            Scene scene = comic.getScenes().get(randomIndex);
            if(!scenes.contains(scene)) scenes.add(scene);
        }
        return scenes;
    }
}

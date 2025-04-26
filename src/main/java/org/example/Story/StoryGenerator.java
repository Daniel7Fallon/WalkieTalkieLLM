package org.example.Story;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;

import java.util.List;

public class StoryGenerator {

    /* Takes numberOfStories scenes from stories spec
     * Generates dialogue for those scenes
     * Returns a comic with those scenes with AI dialogue
     */
    public static Comic generateRandomStoriesComic(Comic originalComic, int numberOfStories) {
        List<Scene> scenes = originalComic.getRandomScenes(numberOfStories);

        Comic outputComic = new Comic();
        outputComic.addAllFigures(originalComic.getFigures());
        outputComic.addAllScenes(scenes);

        List<SceneDialogue> sceneDialogues = outputComic.generateDialogueFromAudioDescriptionComic();

        outputComic.removeAllAboveAndBelow();
        outputComic.replaceDialogue(sceneDialogues);

        return outputComic;
    }

}

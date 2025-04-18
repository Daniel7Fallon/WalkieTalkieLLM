package org.example.Story;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;
import org.example.Utils.ConfigurationFile;
import org.example.XML.XMLParser;

import org.jdom2.JDOMException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryManager {
    static final String STORIES_SPEC = ConfigurationFile.getValue("STORIES_XML");
    static final String STORIES_TARGET = ConfigurationFile.getValue("STORIES_TARGET");
    static final String SOURCE_LANG = ConfigurationFile.getValue("SOURCELANGUAGE");
    static final String TARGET_LANG = ConfigurationFile.getValue("TARGETLANGUAGE");
    static final Random RAND = new Random();

    /* Takes numberOfStories scenes from stories spec
     * Generates dialogue for those scenes
     * Returns a comic with those scenes with AI dialogue
     */
    public static Comic generateRandomStoriesComic(int numberOfStories) throws IOException, JDOMException {
        Comic comic = XMLParser.parseComicFromFilePath(STORIES_SPEC);
        List<Scene> scenes = getRandomScenesFromComic(comic, numberOfStories);

        Comic finalComic = new Comic();
        finalComic.setFigures(comic.getFigures());
        finalComic.addAllScenes(scenes);

        List<SceneDialogue> sceneDialogues = finalComic.generateDialogueFromAudioDescriptionComic();

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

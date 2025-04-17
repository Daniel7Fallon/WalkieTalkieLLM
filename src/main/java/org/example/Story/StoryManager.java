package org.example.Story;

import org.example.Comic.*;
import org.example.Comic.Dialogue.DialogueManager;
import org.example.Comic.Dialogue.SceneDialogue;
import org.example.Translation.Translator;
import org.example.Utils.ConfigurationFile;
import org.example.XML.XMLParser;
import org.example.Comic.ComicPostProcessor;

import org.jdom2.JDOMException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryManager {
    static final String STORIES_SPEC = ConfigurationFile.getValue("STORIES_XML");
    static final String STORIES_TARGET = ConfigurationFile.getValue("STORIES_TARGET");
    static final String SOURCE_LANG = ConfigurationFile.getValue("SOURCELANGUAGE");
    static final String TARGET_LANG = ConfigurationFile.getValue("TARGETLANGUAGE");
    static final Random RAND = new Random();

    public static Comic generateRandomStoriesComic(int numberOfStories) throws IOException, JDOMException {
        Comic comic = loadComicFromXML();
        List<Scene> scenes = getRandomScenesFromComic(comic, numberOfStories);
        DialogueManager.DialogueGenerationResult dialoguesResult = DialogueManager.generateDialogueForScenes(scenes);
        List<SceneDialogue> sceneDialogues = dialoguesResult.getSceneDialogues();
        List<String> allDialogues = dialoguesResult.getAllDialogueLines();

        //Translate dialogues
        try {
            Translator.batchTranslateList(allDialogues);
        } catch (IOException e) {
            System.err.println("Pre-translation failed: " + e.getMessage());
        }

        Comic finalComic = new Comic();
        finalComic.setFigures(comic.getFigures());

        for(int i = 0; i < scenes.size(); i++) {
            Scene bilingualScene = ComicPostProcessor.createBilingualScene(scenes.get(i), sceneDialogues.get(i));
            finalComic.addScene(bilingualScene);
        }

        return finalComic;
    }

    private static Comic loadComicFromXML() throws IOException, JDOMException {
        String xmlContent = new String(Files.readAllBytes(Paths.get(STORIES_SPEC)));
        return XMLParser.parseComic(xmlContent);
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

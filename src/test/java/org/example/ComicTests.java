package org.example;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComicStructureTest {
    private Comic comic;
    private Scene scene;
    private Panel testPanel;

    @BeforeEach
    void setup() {
        comic = new Comic();
        scene = new Scene();
        testPanel = new Panel();
    }

    @Test
    void addAndRemovePanels() {
        // Initial empty state
        assertTrue(comic.getScenes().isEmpty());

        // Add scene with panel
        scene.addPanel(testPanel);
        comic.addScene(scene);

        assertEquals(1, comic.getScenes().size());
        assertEquals(1, comic.getScenes().get(0).getPanels().size());

        // Remove panel
        comic.removeFirstPanel();
        assertTrue(comic.getScenes().get(0).getPanels().isEmpty());
    }

    @Test
    void sectionPanelManagement() {
        // Add base scene
        scene.addPanel(new Panel());
        comic.addScene(scene);

        // Add section panel
        comic.addSectionPanel(1, "Test Section");

        assertEquals(2, comic.getScenes().get(0).getPanels().size());
        Panel sectionPanel = comic.getScenes().get(0).getPanels().get(0);
        assertEquals("Section 1", sectionPanel.getAbove());
        assertEquals("Test Section", sectionPanel.getBelow());
    }
}

class ComicEdgeCasesTest {
    @Test
    void handleEmptyComicOperations() {
        Comic emptyComic = new Comic();

        // Remove from empty comic
        assertFalse(emptyComic.removeFirstPanel());
    }

    @Test
    void invalidPanelManipulations() {
        Comic comic = new Comic();
        Scene scene = new Scene();
        Panel panel = new Panel();

        // Add empty panel
        scene.addPanel(panel);
        comic.addScene(scene);

        // Remove from single-panel scene
        assertTrue(comic.removeFirstPanel());
        assertTrue(comic.getScenes().get(0).getPanels().isEmpty());
    }
}


class ComicDialogueTest {
    @Test
    void emptyDialogueHandling() {
        Comic comic = new Comic();
        List<SceneDialogue> dialogues = comic.generateDialogueFromAudioDescriptionComic();

        assertTrue(dialogues.isEmpty());
    }
}
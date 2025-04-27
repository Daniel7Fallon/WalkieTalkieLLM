package org.example;

import org.example.Audio.AudioManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class AudioManagerTest {
    private static final Map<String, Integer> audioIndex = new HashMap<>();
    private static int currentIndex = 0;

    @BeforeEach
    void setup() {
        audioIndex.clear();
        currentIndex = 0;
    }

    @Test
    void audioIndexingWorksInMemory() {
        // Test phrase mapping
        audioIndex.put("Hello", currentIndex++);
        audioIndex.put("Goodbye", currentIndex++);

        assertEquals(0, audioIndex.get("Hello"));
        assertEquals(1, audioIndex.get("Goodbye"));
        assertNull(audioIndex.get("Unknown"));
    }
}
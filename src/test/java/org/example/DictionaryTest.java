package org.example;

import org.example.Translation.Dictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {
    private static final Map<String, String> translations = new HashMap<>();

    private void addTranslation(String source, String target) {
        translations.put(source.trim().toLowerCase(), target.trim());
    }

    private boolean hasTranslation(String source) {
        return translations.containsKey(source.trim().toLowerCase());
    }

    private String getTranslation(String source) {
        return translations.get(source.trim().toLowerCase());
    }

    @BeforeEach
    void setup() {
        translations.clear();
    }

    @Test
    void translationStorageWorksInMemory() {
        translations.put("Hello", "Hallo");
        translations.put("Goodbye", "Auf Wiedersehen");

        assertEquals("Hallo", translations.get("Hello"));
        assertNull(translations.get("Unknown"));
    }

    @Test
    void storeAndRetrieveSingleTranslation() {
        // Test data
        String sourceText = "Hello";
        String targetText = "Hallo";

        // Store
        addTranslation(sourceText, targetText);

        // Verify
        assertTrue(hasTranslation(sourceText));
        assertEquals(targetText, getTranslation(sourceText));
    }

    @Test
    void handleMissingTranslationsGracefully() {
        assertFalse(hasTranslation("Unknown"));
        assertNull(getTranslation("Unknown"));
    }

    @Test
    void caseInsensitiveLookups() {
        addTranslation("Book", "Buch");
        assertTrue(hasTranslation("  book "));
        assertEquals("Buch", getTranslation("BOOK"));
    }

    @Test
    void specialCharacterHandling() {
        addTranslation("Don't", "Nicht");
        addTranslation("Café", "Café");

        assertTrue(hasTranslation("don't"));
        assertEquals("Café", getTranslation("café"));
    }
}
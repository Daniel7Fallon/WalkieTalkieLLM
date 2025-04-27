package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TranslationFlowTest {
    private static final Map<String, String> englishToGerman = new HashMap<>();
    private static final Map<String, String> germanToFrench = new HashMap<>();

    @BeforeEach
    void setup() {
        englishToGerman.clear();
        germanToFrench.clear();

        // Seed test data
        englishToGerman.put("hello", "hallo");
        germanToFrench.put("hallo", "bonjour");
    }

    @Test
    void directTranslationFlow() {
        // ENGLISH -> GERMAN
        assertEquals("hallo", englishToGerman.get("hello"));
    }

    @Test
    void chainedTranslationFlow() {
        // ENGLISH -> GERMAN -> FRENCH
        String german = englishToGerman.get("hello");
        String french = germanToFrench.get(german);

        assertEquals("hallo", german);
        assertEquals("bonjour", french);
    }

    @Test
    void handleMissingChainLinks() {
        assertNull(englishToGerman.get("unknown"));
        assertNull(germanToFrench.get("unbekannt"));
    }
}

class TranslationValidationTest {
    private Map<String, String> translations;

    @BeforeEach
    void setup() {
        translations = new HashMap<>(); // Fresh map for each test
    }

    @Test
    void validateTranslationStorage() {
        translations.put("cat", "Katze");
        translations.put("dog", "Hund");

        assertEquals(2, translations.size());
        assertTrue(translations.containsKey("cat"));
        assertTrue(translations.containsValue("Hund"));
    }

    @Test
    void preventDuplicateEntries() {
        translations.put("apple", "Apfel");
        translations.put("apple", "Äpfel"); // Overwrite

        assertEquals(1, translations.size());
        assertEquals("Äpfel", translations.get("apple"));
    }

    @Test
    void handleEmptyValues() {
        translations.put("", "leer");
        translations.put("empty", "");

        assertTrue(translations.containsKey(""));
        assertTrue(translations.containsValue(""));
    }
}
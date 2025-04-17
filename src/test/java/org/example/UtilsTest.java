package org.example;

import org.example.Utils.StringUtil;
import org.junit.jupiter.api.Test;
import org.example.Utils.StringUtil.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {
    @Test
    void testStringUtilCapitalize() {
        String s1 = "aaa";
        String s2 = "AAA";
        String s3 = "aAa";
        assertEquals("Aaa", StringUtil.capitalize(s1));
        assertEquals("Aaa", StringUtil.capitalize(s2));
        assertEquals("Aaa", StringUtil.capitalize(s3));
    }

    @Test
    void testremovePluralIdentifier() {
        assertEquals("apple", StringUtil.removePluralIdentifier("apple (Plural)"));
        assertEquals("book", StringUtil.removePluralIdentifier("book (plural)"));
        assertEquals("cat", StringUtil.removePluralIdentifier("cat"));
    }

    @Test
    void testremoveSpeakerFromDialogue() {
        assertEquals("Hello world!", StringUtil.removeSpeaker("John: Hello world!"));
        assertEquals("How are you?", StringUtil.removeSpeaker("  Sarah :   How are you?  "));
        assertEquals("No speaker", StringUtil.removeSpeaker("No speaker"));
    }
}

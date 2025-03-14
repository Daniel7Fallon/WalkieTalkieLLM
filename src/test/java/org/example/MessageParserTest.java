package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {

    @Test
    void testNumberedList_SingleLine() {
        String input = "1. First item 2. Second item 3. Third item";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_MultiLine() {
        String input = "Certainly! Here is what you asked for: 1. First item\n2. Second item\n3. Third item";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_EmptyInput() {
        String input = "";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        assertTrue(result.isEmpty());
    }

    @Test
    void testNumberedList_NoNumbers() {
        String input = "This is not a numbered list.";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        assertTrue(result.isEmpty());
    }

    @Test
    void testNumberedList_ExtraSpaces() {
        String input = "1.   First item   2.  Second item   3. Third item";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_MissingText() {
        String input = "1. 2. Second item 3.";

        MessageParser.NumberedList numberedList = MessageParser.createNumberedList(input);
        List<String> result = numberedList.getItems();

        for (String item: result) {
            System.out.println(item);
        }

        assertEquals(1, result.size());
        assertEquals("Second item", result.get(0));
    }
}
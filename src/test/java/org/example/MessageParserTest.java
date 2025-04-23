package org.example;

import org.example.Utils.NumberedList;
import org.example.Utils.StringUtil;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {

    @Test
    void testNumberedList_SingleLine() {
        String input = "1. First item 2. Second item 3. Third item";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_MultiLine() {
        String input = "Certainly! Here is what you asked for: 1. First item\n2. Second item\n3. Third item";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_EmptyInput() {
        String input = "";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        assertTrue(result.isEmpty());
    }

    @Test
    void testNumberedList_NoNumbers() {
        String input = "This is not a numbered list.";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        assertTrue(result.isEmpty());
    }

    @Test
    void testNumberedList_ExtraSpaces() {
        String input = "1.   First item   2.  Second item   3. Third item";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        assertEquals(3, result.size());
        assertEquals("First item", result.get(0));
        assertEquals("Second item", result.get(1));
        assertEquals("Third item", result.get(2));
    }

    @Test
    void testNumberedList_MissingText() {
        String input = "1. 2. Second item 3.";

        NumberedList numberedList = new NumberedList();
        numberedList.addAll(StringUtil.parseNumberedList(input));
        List<String> result = numberedList.getList();

        for (String item: result) {
            System.out.println(item);
        }

        assertEquals(1, result.size());
        assertEquals("Second item", result.get(0));
    }

    @Test
    void testNumberedList_WithSlashSeparatedList() {
        String input = "1. ein Charmeur / eine Charmeur\n2. der Hund / die Hündin";
        List<String> result = StringUtil.parseNumberedList(input);
        assertEquals(List.of("ein Charmeur", "der Hund"), result);
    }

    @Test
    void testNumberedList_SlashWithSpaces() {
        String input = "1. option1 / option2\n2. choiceA / choiceB";
        List<String> result = StringUtil.parseNumberedList(input);
        assertEquals(List.of("option1", "choiceA"), result);
    }

    @Test
    void testNumberedList_MultipleSlashes() {
        String input = "1. cat/dog/mouse\n2. red/green/blue";
        List<String> result = StringUtil.parseNumberedList(input);
        assertEquals(List.of("cat", "red"), result);
    }

    @Test
    void testNumberedList_SlashOnly() {
        List<String> result1 = StringUtil.parseNumberedList("1. / empty option");
        assertTrue(result1.isEmpty(), "Should handle empty first part");

        List<String> result2 = StringUtil.parseNumberedList("1.    /   ");
        assertTrue(result2.isEmpty(), "Should handle whitespace-only");
    }

    @Test
    void testNumberedList_SlashAtEnd() {
        List<String> result = StringUtil.parseNumberedList("1. First item/");
        assertEquals(List.of("First item"), result);
    }

    @Test
    void testNumberedList_MixedFormats() {
        String input = "1. Normal item 2. Multi/choice 3. Third item";
        List<String> result = StringUtil.parseNumberedList(input);
        assertEquals(List.of("Normal item", "Multi", "Third item"), result);
    }

}
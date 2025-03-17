package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    // Can change the expression later
    private static final String NUMBERED_LIST_REGEX = "\\d+\\.\\s*([^\\d]+?)(?=\\s*\\d+\\.|$)";

    private static List<String> parseNumberedList(String input) {
        List<String> result = new ArrayList<>();

        // Regular expression to match numbered list items
        Pattern pattern = Pattern.compile(NUMBERED_LIST_REGEX);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String item = matcher.group(1).trim();
            if (!item.isEmpty()) {
                result.add(item);
            }
        }

        return result;
    }

    // Return a class containing the numbered list
    public static NumberedList createNumberedList(String input) {
        NumberedList output = new NumberedList();
        if (input == null || input.trim().isEmpty()) {
            return output;
        }
        List<String> items = parseNumberedList(input);
        for (String item : items) {
            output.add(item);
        }
        return output;
    }

    // Will add more methods relevant to processing responses as needed
}

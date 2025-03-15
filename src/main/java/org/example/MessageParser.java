package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    // Can change the expression later
    private static final String NUMBERED_LIST_REGEX = "\\d+\\.\\s*([^\\d]+?)(?=\\s*\\d+\\.|$)";

    public static class NumberedList {
        private final List<String> items;

        public NumberedList(List<String> items) {
            // Make the list immutable for good measure
            this.items = Collections.unmodifiableList(items);
        }

        public List<String> getItems() {
            return items;
        }
    }

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
        if (input == null || input.trim().isEmpty()) {
            return new NumberedList(Collections.emptyList());
        }
        List<String> items = parseNumberedList(input);
        return new NumberedList(items);
    }

    // Will add more methods relevant to processing responses as needed
}

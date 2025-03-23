package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    // Can change the expression later
    private static final String NUMBERED_LIST_REGEX = "\\d+\\.\\s*(.*?)(?=\\s*\\d+\\.|$)";

    public static List<String> parseNumberedList(String input) {
        if(input == null || input.trim().isEmpty()) return Collections.emptyList();

        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(NUMBERED_LIST_REGEX); // Regular expression to match numbered list items
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String item = matcher.group(1).trim();
            if (!item.isEmpty()) {
                String[] parts = item.split("/", 2);
                String primary = parts[0].trim();
                if (!primary.isEmpty()) {
                    result.add(primary);
                }
            }
        }

        return result;
    }

    // Will add more methods relevant to processing responses as needed
}

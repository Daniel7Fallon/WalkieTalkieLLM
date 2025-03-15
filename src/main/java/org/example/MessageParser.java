package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {

    public static List<String> parseNumberedList(String input) {
        List<String> result = new ArrayList<>();

        // Regular expression to match numbered list items
        Pattern pattern = Pattern.compile("\\d+\\.\\s*([^\\d]+?)(?=\\s*\\d+\\.|$)"); // Can change the expression later
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String item = matcher.group(1).trim();
            if (!item.isEmpty()) {
                result.add(item);
            }
        }

        return result;
    }

    // Will add more methods relevant to processing responses as needed
}

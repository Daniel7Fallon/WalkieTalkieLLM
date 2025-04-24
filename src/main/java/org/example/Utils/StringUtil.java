package org.example.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    // Can change the expression later
    private static final String NUMBERED_LIST_REGEX = "\\d+\\.\\s*(.*?)(?=\\s*\\d+\\.|$)";

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String removePluralIdentifier(String str) {
        return str.replace(" (Plural)", "").replace(" (plural)", "");
    }

    public static String removeSpeaker(String str) {
        return str.replaceFirst("^\\s*[^:]+:\\s*", "").trim();
    }

    public static String clean(String str) {
        if(str == null) return null;
        return str.replaceAll("[‘’´`]", "'")
                .replaceAll("[“”]", "\"")
                .replaceAll("\\p{C}", "");
    }

    public static List<String> cleanList(List<String> list) {
        if(list == null) return null;
        List<String> cleanedList = new ArrayList<>();
        for(String s : list) {
            cleanedList.add(clean(s));
        }
        return cleanedList;
    }

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
}

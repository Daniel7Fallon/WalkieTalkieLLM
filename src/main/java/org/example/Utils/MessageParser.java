package org.example.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
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

    public static List<List<String>> parseNumberedDialogue(String input) {
        List<List<String>> panels = new ArrayList<>();

        Pattern panelPattern = Pattern.compile("^(\\d+)\\.\\s*(.*)$");
        Pattern dialoguePattern = Pattern.compile("(\\w+):\\s*\"([^\"]*)\"");

        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            Matcher panelMatcher = panelPattern.matcher(line);
            if (panelMatcher.matches()) {
                String content = panelMatcher.group(2);
                Matcher dialogueMatcher = dialoguePattern.matcher(content);

                List<String> panelLines = new ArrayList<>();
                while (dialogueMatcher.find()) {
                    String speaker = dialogueMatcher.group(1);
                    String text = dialogueMatcher.group(2);
                    panelLines.add(speaker + ": " + text);
                }

                panels.add(panelLines);
            }
        }

        return panels;
    }

    public static String parseSingleTranslation(String response) {
        // Used for handling response formats
        Pattern pattern = Pattern.compile("\"?([^\"]+?)\"?$");
        Matcher matcher = pattern.matcher(response.trim());
        return matcher.find() ? matcher.group(1) : response;
    }

    // Will add more methods relevant to processing responses as needed
}

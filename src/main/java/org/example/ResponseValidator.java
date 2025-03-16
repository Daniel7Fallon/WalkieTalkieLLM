package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ResponseValidator {
    private static final List<Pattern> DENIAL_PATTERNS = Arrays.asList(
            Pattern.compile("(?i)(I'm\\s+sorry,?\\s+but\\s+)?(as\\s+an?\\s+ai\\s+language\\s+model|I)\\s+(can't|cannot|can\\s+not)\\s+.*"),
            Pattern.compile("(?i)(I'm|I\\s+am)\\s+(unable|not\\s+allowed|not\\s+permitted|prohibited)\\s+to\\s+.*"),
            Pattern.compile("(?i)This\\s+(request|question|content).*?(against|violates)\\s+(my|our|the)"),
            Pattern.compile("(?i)(My\\s+purpose|I\\s+was\\s+created|My\\s+programming).*?(don't|do\\s+not|cannot)\\s+.*"),
            Pattern.compile("(?i)against\\s+my\\s+(ethical|programming|operational)\\s+guidelines"),
            Pattern.compile("(?i)apologize,?\\s+but\\s+I\\s+(can't|cannot)"),
            Pattern.compile("(?i)(unable\\s+to\\s+comply|decline\\s+to\\s+provide)")
    );

    public static boolean isDenial(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        String clean = response
                .replaceAll("[?]", "'")
                .replaceAll("[‘’´`]", "'")
                .replaceAll("[“”]", "\"")
                .replaceAll("\\p{C}", "");

        for (Pattern pattern : DENIAL_PATTERNS) {
            if (pattern.matcher(clean).find()) {
                return true;
            }
        }

        return false;
    }
}

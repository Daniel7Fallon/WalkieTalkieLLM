package org.example.Completion;

import org.example.Utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ResponseValidator {
    private static final List<Pattern> DENIAL_PATTERNS = Arrays.asList(
            Pattern.compile("(?i)^\\b(as\\s+an?\\s+ai\\s+language\\s+model)\\b.*"),
            Pattern.compile("(?i)^\\b(I\\s+(am\\s+)?(unable|not\\s+allowed|not\\s+permitted|prohibited|not\\s+capable)\\s+to)\\b.*"),
            Pattern.compile("(?i)^\\b(This\\s+(request|question|content).*?(against|violates))\\b.*"),
            Pattern.compile("(?i)^\\b(My\\s+purpose|I\\s+was\\s+created|My\\s+programming).*?(don't|do\\s+not|cannot)\\b.*"),
            Pattern.compile("(?i)^\\b(against\\s+my\\s+(ethical|programming|operational)\\s+guidelines)\\b.*"),
            Pattern.compile("(?i)^\\b(I\\s+apologize,?\\s+but\\s+I\\s+(can't|cannot))\\b.*"),
            Pattern.compile("(?i)^\\b(I\\s+(decline|refuse)\\s+to\\s+(answer|comply|respond))\\b.*"),
            Pattern.compile("(?i)^\\bI\\s+(can't|cannot|can\\s+not)\\s+(help\\s+(you|with|in)|provide|assist|answer|comply|respond)\\b.*")
    );

    public static boolean isDenial(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }
        String clean = StringUtil.clean(response);
        for (Pattern pattern : DENIAL_PATTERNS) {
            if (pattern.matcher(clean).find()) {
                return true;
            }
        }
        return false;
    }
}

package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ResponseValidator {
    private static final List<Pattern> DENIAL_PATTERNS = Arrays.asList(
            Pattern.compile("(?i)Sorry,? (as an? AI language model|I) (can't|cannot) .*"),
            Pattern.compile("(?i)(I'm|I am) (unable|not allowed) to .*"),
            Pattern.compile("(?i)This (content|request) (is against|violates) .*"),
            Pattern.compile("(?i)(My purpose|I was created) .* (don't|do not) (assist|help)"),
            Pattern.compile("(?i)against (my (programming|ethical) guidelines)")
    );

    public static boolean isDenial(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        for (Pattern pattern : DENIAL_PATTERNS) {
            if (pattern.matcher(response).find()) {
                return true;
            }
        }

        return false;
    }


}

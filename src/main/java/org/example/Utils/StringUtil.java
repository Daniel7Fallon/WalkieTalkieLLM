package org.example.Utils;

public class StringUtil {

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String removePluralIdentifier(String str) {
        return str.replace(" (Plural)", "").replace(" (plural)", "");
    }

    public static String removeSpeaker(String str) {
        return str.replaceFirst("^\\s*[^:]+:\\s*", "").trim();
    }
}

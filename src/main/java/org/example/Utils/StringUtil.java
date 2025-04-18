package org.example.Utils;

import java.util.ArrayList;
import java.util.List;

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

    public static String clean(String str) {
        if(str == null) return null;
        return str.replaceAll("[‘’´`]", "'")
                .replaceAll("[“”]", "\"")
                .replaceAll("\\p{C}", "");
    }

    public static List<String> cleanList(List<String> list) {
        if(list == null) return null;
        List<String> cleanedList = new ArrayList<String>();
        for(String s : list) {
            cleanedList.add(clean(s));
        }
        return cleanedList;
    }
}

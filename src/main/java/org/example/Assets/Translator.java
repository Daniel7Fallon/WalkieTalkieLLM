package org.example.Assets;

import org.example.Completion.CompletionSession;
import org.example.ConfigurationFile;
import org.example.MessageParser;
import org.example.NumberedList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Translator {

    public static void translateVignetteList(List<Vignette> vignettes, String filePath) {
        try(FileWriter writer = new FileWriter(filePath, true)) {

            NumberedList sourceTexts = new NumberedList();

            StringBuilder messageContent = new StringBuilder();
            messageContent.append("I am going to give you a numbered list of words or phrases in "
                    + ConfigurationFile.getValue("SOURCELANGUAGE").toLowerCase()
                    + " and I would like you to return a numbered list of those words or phases translated into "
                    + ConfigurationFile.getValue("TARGETLANGUAGE").toLowerCase()
                    + ".\n"
                    + "Do this such that the translations have the same indicies in the new list as in the given list.\n");

            int i = 0;
            for (Vignette vignette : vignettes) {
                for (String leftText : vignette.getLeftText()) {
                    if (!translationExists(leftText, filePath)) {
                        messageContent.append(++i).append(". " + leftText + "\n");
                        sourceTexts.add(leftText);
                    }
                }
            }
            System.out.println(messageContent.toString());
            CompletionSession translationSession = new CompletionSession();
            String response = translationSession.sendMessage("user", messageContent.toString());
            System.out.println(response);

            NumberedList numberedList = MessageParser.createNumberedList(response);
            System.out.println(numberedList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean translationExists(String sourceText, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens[0].equals(sourceText)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}

package org.example;

import org.example.Assets.Vignette;
import org.example.Completion.CompletionSession;

import java.io.*;
import java.util.List;

public class Translator {

    //Translates the leftText entries from the given vignettes and serialises them at the file path given
    //Creates file if it doesn't exist and appends the translations
    //The format is <sourceText>\t<targetText>
    public static void translateVignetteList(List<Vignette> vignettes, String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            boolean created = file.createNewFile();
            if(created) {
                System.out.println("File created: " + filePath);
            } else {
                System.out.println("File not created: " + filePath);
            }
        } else {
            System.out.println("Translations file found: " + filePath);
        }

        NumberedList sourceTexts = new NumberedList();

        //Build message for CompletionSession
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
                } else {
                    System.out.println("Match found: " + leftText);
                }
            }
        }
        CompletionSession translationSession = new CompletionSession();
        String response = translationSession.sendMessage("user", messageContent.toString());

        NumberedList targetTexts = new NumberedList();
        targetTexts.addAll(MessageParser.parseNumberedList(response));

        System.out.println("Source Texts to write: \n" + sourceTexts);
        System.out.println("Target Texts to write: \n" + targetTexts);
        appendTranslations(sourceTexts, targetTexts, filePath);
    }

    public static boolean translationExists(String sourceText, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (sourceText.equals(tokens[0].trim())) return true;
            }
        }
        return false;
    }

    private static void appendTranslations(NumberedList sourceList, NumberedList targetList, String filePath) throws IOException {
        if(sourceList.size() != targetList.size()) throw new IllegalArgumentException("Source text list and target text list of different sizes.");
        try(FileWriter writer = new FileWriter(filePath, true)) {
            for (int i = 1; i <= sourceList.size(); i++) {
                String line = sourceList.getByPosition(i) + "\t" + targetList.getByPosition(i);
                System.out.println("New Translation: " + line);
                writer.write(line + "\n");
            }
        }
    }

    public static String getTranslation(String sourceText, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (sourceText.equals(tokens[0].trim())) return tokens[1].trim();
            }
        }
        return null;
    }
}

package org.example;

import org.example.Assets.Vignette;
import org.example.Completion.CompletionSession;

import java.io.*;
import java.util.List;

public class Translator {

    //Translates the leftText entries from the given vignettes and serialises them at the file path given
    //Creates file if it doesn't exist and appends the translations
    //The format is <sourceText>\t<targetText>
    public static void translateVignetteList(List<Vignette> vignettes, String sourceLanguage, String targetLanguage) throws IOException {
        Dictionary.createNewTranslationFile(sourceLanguage, targetLanguage);
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
                if (!Dictionary.translationExists(leftText, sourceLanguage, targetLanguage)) {
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
        Dictionary.appendTranslations(sourceLanguage, sourceTexts, targetLanguage, targetTexts);
    }




}

package org.example;

import org.example.Assets.VignetteSchema;
import org.example.Completion.CompletionSession;

import java.io.*;
import java.util.List;

public class Translator {
    //Translates the leftText entries from the given vignettes and serialises them.
    //EnglishTo<Target> if source is English
    //EnglishTo<Source>, <Source>To<Target> if english is not source
    public static void translateListAndWrite(List<String> input) throws IOException {
        String sourceLanguage = ConfigurationFile.getValue("SOURCELANGUAGE");
        String targetLanguage = ConfigurationFile.getValue("TARGETLANGUAGE");
        if(sourceLanguage.equals("English")) {
            Dictionary.createNewTranslationFile(sourceLanguage, targetLanguage);//Ensure file exists
            NumberedList sourceTexts = new NumberedList();
                for (String leftText : input) {
                    if (!Dictionary.translationExists(leftText, sourceLanguage, targetLanguage)) {
                        sourceTexts.add(leftText);
                }
            }
            if (sourceTexts.isEmpty()) {
                System.out.println("All translations from " + sourceLanguage + " to " + targetLanguage + " already exist.");
            } else {
                NumberedList targetTexts = translateNumberedList(sourceTexts, sourceLanguage, targetLanguage);
                Dictionary.appendTranslations(sourceLanguage, sourceTexts, targetLanguage, targetTexts);
            }

        } else {//English is not source language
            Dictionary.createNewTranslationFile("English", sourceLanguage);//Ensure file exists
            NumberedList englishTexts = new NumberedList();
            for (String leftText : input) {
                if (!Dictionary.translationExists(leftText, "English", sourceLanguage)) {
                    englishTexts.add(leftText);
                }
            }
            if (englishTexts.isEmpty()) {
                System.out.println("All translations from English to " + sourceLanguage + " already exist.");
            } else {
                NumberedList sourceTexts = translateNumberedList(englishTexts, "English", sourceLanguage);
                Dictionary.appendTranslations("English", englishTexts, sourceLanguage, sourceTexts);
            }

            //Second translation:
            NumberedList fullSourceList = new NumberedList();
            for (String leftText : input) {
                fullSourceList.add(Dictionary.getTranslation(leftText, "English", sourceLanguage));
            }

            Dictionary.createNewTranslationFile(sourceLanguage, targetLanguage);
            NumberedList sourceTexts = new NumberedList();

            for(String sourceText: fullSourceList.getList()) {
                if (!Dictionary.translationExists(sourceText, sourceLanguage, targetLanguage)) {
                    sourceTexts.add(sourceText);
                }
            }
            if (sourceTexts.isEmpty()) {
                System.out.println("All translations from " + sourceLanguage + " to " + targetLanguage + " already exist.");
            } else {
                NumberedList targetTexts = translateNumberedList(sourceTexts, sourceLanguage, targetLanguage);
                Dictionary.appendTranslations(sourceLanguage, sourceTexts, targetLanguage, targetTexts);
            }
        }
    }

    private static NumberedList translateNumberedList(NumberedList sourceTexts, String sourceLanguage, String targetLanguage) throws IOException {
        //Build message for CompletionSession
        if (sourceTexts.size() == 0) {
            return new NumberedList();
        }
        StringBuilder messageContent = new StringBuilder();
        messageContent.append("I am going to give you a numbered list of words or phrases in "
                + sourceLanguage
                + " and I would like you to return a numbered list of those words or phases translated into "
                + targetLanguage
                + ".\n"
                + "Do this such that the translations have the same indicies in the new list as in the given list.\n");

        int i = 0;
        for (String sourceText : sourceTexts.getList()) {
            messageContent.append(++i).append(". " + sourceText + "\n");
        }

        CompletionSession translationSession = new CompletionSession();
        String response = translationSession.sendMessage("user", messageContent.toString());

        NumberedList targetTexts = new NumberedList();
        targetTexts.addAll(MessageParser.parseNumberedList(response));
        return targetTexts;
    }
}

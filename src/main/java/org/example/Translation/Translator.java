package org.example.Translation;

import org.example.Utils.StringUtil;
import org.example.Completion.CompletionSession;
import org.example.Utils.ConfigurationFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Translator {

    /**
     * Translates a list of input strings in batches of a configured size.
     * Checks the dictionary first and only translates items not already present.
     *
     * @param input The list of strings to translate.
     * @throws IOException if dictionary file operations fail or configuration values are missing/invalid.
     */
    public static void batchTranslateList(List<String> input) throws IOException{
        int batchSize = Integer.parseInt(ConfigurationFile.getValue("TRANSLATION_BATCH_SIZE"));
        List<String> batch = new ArrayList<>();
        //Translate in batches
        for(int i = 0; i < input.size(); i++) {
            batch.add(input.get(i));
            if(batch.size() == batchSize) {
                translateListAndWrite(batch);
                batch = new ArrayList<>();
            }
        }
        //Translate last batch if it doesn't reach max size
        if(!batch.isEmpty()) {
            translateListAndWrite(batch);
        }
    }


    /**
     * Translates a single batch of input strings and writes new translations to the dictionary.
     * Handles direct translation (if source is English) or two-step translation
     * (English -> Source -> Target) if source is not English. Filters out items already in Dictionary.
     *
     * @param input The batch of strings to translate (should not be empty).
     * @throws IOException if dictionary or translation operations fail.
     */
    private static void translateListAndWrite(List<String> input) throws IOException {
        String sourceLanguage = ConfigurationFile.getValue("SOURCE_LANGUAGE");
        String targetLanguage = ConfigurationFile.getValue("TARGET_LANGUAGE");
        if(sourceLanguage.equals("English")) {
            Dictionary.createNewTranslationFile(sourceLanguage, targetLanguage);//Ensure file exists
            NumberedList sourceTexts = new NumberedList();
                for (String leftText : input) {
                    if (!Dictionary.translationExists(leftText, sourceLanguage, targetLanguage)) {
                        sourceTexts.add(leftText);
                }
            }
            if (!sourceTexts.isEmpty()) {
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
            if (!englishTexts.isEmpty()) {
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
            if (!sourceTexts.isEmpty()) {
                NumberedList targetTexts = translateNumberedList(sourceTexts, sourceLanguage, targetLanguage);
                Dictionary.appendTranslations(sourceLanguage, sourceTexts, targetLanguage, targetTexts);
            }
        }
    }

    /**
     * Sends a numbered list of source texts to the completion service for translation.
     * Parses the numbered list response. Returns empty list if sourceTexts is empty.
     *
     * @param sourceTexts The NumberedList of texts to translate (shouldn't be null).
     * @param sourceLanguage The source language.
     * @param targetLanguage The target language.
     * @return A NumberedList containing the translated texts, potentially empty.
     * @throws RuntimeException if the completion service call fails (from CompletionSession).
     */
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
        targetTexts.addAll(StringUtil.parseNumberedList(response));
        return targetTexts;
    }

}

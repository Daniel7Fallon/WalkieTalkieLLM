package org.example;

import org.example.Assets.Vignette;
import org.example.Completion.CompletionSession;

import java.io.*;
import java.util.List;

public class Translator {
    //Translates the leftText entries from the given vignettes and serialises them.
    //EnglishTo<Target> if source is English
    //EnglishTo<Source>, <Source>To<Target> if english is not source
    public static void translateVignetteList(List<Vignette> vignettes) throws IOException {
        String sourceLanguage = ConfigurationFile.getValue("SOURCELANGUAGE");
        String targetLanguage = ConfigurationFile.getValue("TARGETLANGUAGE");

        String sLang = "English";
        String tLang;
        //First translation:
        //If source == English => English to target
        //If source != English => English to source
        if(sourceLanguage.equals("English")) {
            tLang = targetLanguage;
        } else {
            tLang = sourceLanguage;
        }
        Dictionary.createNewTranslationFile(sLang, tLang);//Ensure file exists
        NumberedList sourceTexts = new NumberedList();
        for (Vignette vignette : vignettes) {
            for (String leftText : vignette.getLeftText()) {
                if (!Dictionary.translationExists(leftText, sLang, tLang)) {
                    sourceTexts.add(leftText);
                }
            }
        }
        NumberedList targetTexts = translateNumberedList(sourceTexts, sLang, tLang);
        Dictionary.appendTranslations(sLang, sourceTexts, tLang, targetTexts);

        //Second translation:
        //If source != English => source to target
        if(!sourceLanguage.equals("English")) {
            sLang = sourceLanguage;
            tLang = targetLanguage;
            Dictionary.createNewTranslationFile(sLang, tLang);//Ensure file exists
            //Output from previous translation is new input
            sourceTexts = new NumberedList();
            for (String sourceText: targetTexts.getList()) {
                if (!Dictionary.translationExists(sourceText, sLang, tLang)) {
                    sourceTexts.add(sourceText);
                }
            }
            targetTexts = translateNumberedList(sourceTexts, sLang, tLang);
            Dictionary.appendTranslations(sLang, sourceTexts, tLang, targetTexts);
        }
    }

    private static NumberedList translateNumberedList(NumberedList sourceTexts, String sourceLanguage, String targetLanguage) throws IOException {
        //Build message for CompletionSession
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

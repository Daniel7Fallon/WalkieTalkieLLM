package org.example.Translation;

import org.example.Utils.ConfigurationFile;
import org.example.Utils.StringUtil;

import java.io.*;

public class Dictionary {
    private static String translationsDirectoryPath;

    /**
     * Initialises the Dictionary by setting the translations directory path from configuration
     * and ensuring the directory exists.
     */
    public static void initialize() {
        translationsDirectoryPath = ConfigurationFile.getValue("TRANSLATIONS_FOLDER");
        File translationsDirectory = new File(translationsDirectoryPath);
        if (!translationsDirectory.exists()) {
            if (translationsDirectory.mkdirs()) {
                System.out.println("Translations folder created: " + translationsDirectoryPath);
            } else {
                System.out.println("Translations folder could not be created: " + translationsDirectoryPath);
            }
        } else if(!translationsDirectory.isDirectory()) {
            System.out.println("The specified translations folder is not a directory: " + translationsDirectoryPath);
        }
    }

    /**
     * Creates a new, empty translation file for the given language pair if it doesn't already exist.
     * The filename is in the following format: "<sourceLanguage>To<targetLanguage>.txt".
     *
     * @param sourceLanguage The source language.
     * @param targetLanguage The target language.
     * @throws IOException if an error occurs during file creation.
     */
    public static void createNewTranslationFile(String sourceLanguage, String targetLanguage) throws IOException {
        String fileName = sourceLanguage + "To" + targetLanguage + ".txt";
        File file = new File(translationsDirectoryPath, fileName);
        try {
            if(file.createNewFile()) {
                System.out.println("Translation file created: " + fileName);
            }
        } catch (IOException e) {
            throw new IOException("Error creating translation file: " + e.getMessage());
        }
    }

    /**
     * Checks if a translation for the given source text exists in the specified language pair file.
     * Compares against the first tab-separated token on each line after cleaning the input text.
     *
     * @param sourceText The text to check for translation.
     * @param sourceLanguage The source language.
     * @param targetLanguage The target language.
     * @return true if a matching source text entry is found, false otherwise.
     * @throws IOException if an error occurs reading the translation file.
     */
    public static boolean translationExists(String sourceText, String sourceLanguage, String targetLanguage) throws IOException {
        String cleanSourceText = StringUtil.clean(sourceText);
        String fileName = sourceLanguage + "To" + targetLanguage + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(translationsDirectoryPath + "/" + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (cleanSourceText.equals(tokens[0].trim())) return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the translation for a given source text from the specified language pair file.
     * Returns the second tab-separated token from the matching line.
     *
     * @param sourceText The text whose translation is needed.
     * @param sourceLanguage The source language.
     * @param targetLanguage The target language.
     * @return The translated text if found, otherwise null.
     * @throws IOException if an error occurs reading the translation file.
     */
    public static String getTranslation(String sourceText, String sourceLanguage, String targetLanguage) throws IOException {
        String cleanSourceText = StringUtil.clean(sourceText);
        String fileName = sourceLanguage + "To" + targetLanguage + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(translationsDirectoryPath + "/" + fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (cleanSourceText.equals(tokens[0].trim())) return tokens[1].trim();
            }
        }
        return null;
    }

    /**
     * Appends translation pairs from source and target NumberedLists to the appropriate file.
     * Cleans entries before writing. Assumes lists are parallel and of the same size.
     *
     * @param sourceLanguage The source language.
     * @param sourceList A NumberedList containing source texts.
     * @param targetLanguage The target language.
     * @param targetList A NumberedList containing corresponding target texts.
     * @throws IOException if an error occurs writing to the translation file.
     * @throws IllegalArgumentException if the source and target lists have different sizes.
     */
    public static void appendTranslations(String sourceLanguage, NumberedList sourceList, String targetLanguage, NumberedList targetList) throws IOException {
        NumberedList cleanSourceList = sourceList.cleanEntries();
        NumberedList cleanTargetList = targetList.cleanEntries();
        if(cleanSourceList.size() != cleanTargetList.size()) throw new IllegalArgumentException("Source text list and target text list of different sizes.");
        String fileName = sourceLanguage + "To" + targetLanguage + ".txt";
        try(FileWriter writer = new FileWriter(translationsDirectoryPath + "/" + fileName, true)) {
            for (int i = 1; i <= cleanSourceList.size(); i++) {
                String line = cleanSourceList.getByPosition(i) + "\t" + cleanTargetList.getByPosition(i);
                writer.write(line + "\n");
            }
        }
    }

    /**
     * Retrieves both the source and target language versions of a text based on configuration.
     * Handles cases where the configured source language is English or another language
     * (which requires an intermediate English translation step).
     *
     * @param text The input text (assumed to be in English if sourceLang is not English).
     * @return A String array [sourceText, targetText], or null if any required translation step fails.
     * @throws IOException if an error occurs reading translation files.
     */
    public static String[] getSourceAndTargetTranslations(String text) throws IOException{
        String sourceLang = ConfigurationFile.getValue("SOURCE_LANGUAGE");
        String targetLang = ConfigurationFile.getValue("TARGET_LANGUAGE");
        String[] output = new String[2];
        if(sourceLang.equals("English")) {
            output[0] = text;
            output[1] = getTranslation(text, sourceLang, targetLang);
            if(output[1] == null) return null;
        } else {
            output[0] = getTranslation(text, "English", sourceLang);
            output[1] = getTranslation(output[0], sourceLang, targetLang);
            if(output[0] == null || output[1] == null) return null;
        }
        return output;
    }
}

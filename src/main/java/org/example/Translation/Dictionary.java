package org.example.Translation;

import org.example.Utils.ConfigurationFile;
import org.example.Utils.StringUtil;

import java.io.*;

public class Dictionary {
    private static String translationsDirectoryPath;

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

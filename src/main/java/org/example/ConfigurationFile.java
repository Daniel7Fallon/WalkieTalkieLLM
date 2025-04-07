package org.example;

import org.example.Utils.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ConfigurationFile {
    //Stores configuration
    private static final Map<String, String> configMap = new HashMap<>();

    //Must be called before any other method
    public static void initialize(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split("\t", 2);
                if(tokens.length == 2) {
                    String key = tokens[0].trim();
                    String value = tokens[1].trim();
                    configMap.put(key, value);
                }
            }
            System.out.println("Configuration file loaded successfully.");

        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }

        String sourceLanguage = configMap.get("SOURCELANGUAGE");
        if(sourceLanguage == null || sourceLanguage.isEmpty()) throw new IllegalArgumentException("SOURCELANGUAGE parameter is missing in configuration file.");
        String targetLanguage = configMap.get("TARGETLANGUAGE");
        if(targetLanguage == null || targetLanguage.isEmpty()) throw new IllegalArgumentException("TARGETLANGUAGE parameter is missing in configuration file.");
        String completionsUrl = configMap.get("COMPLETIONS_URL");
        if(completionsUrl == null || completionsUrl.isEmpty()) throw new IllegalArgumentException("COMPLETIONS_URL parameter is missing in configuration file.");
        String apiKey = configMap.get("API_KEY");
        if(apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("API_KEY parameter is missing in configuration file.");
        String model = configMap.get("MODEL");
        if(model == null || model.isEmpty()) throw new IllegalArgumentException("MODEL parameter is missing in configuration file.");
        String translationsFolder = configMap.get("TRANSLATIONS_FOLDER");
        if(translationsFolder == null || translationsFolder.isEmpty()) throw new IllegalArgumentException("TRANSLATIONS_FOLDER parameter is missing in configuration file.");

        String translationBatchSize = configMap.get("TRANSLATION_BATCH_SIZE");
        if(translationBatchSize == null || translationBatchSize.isEmpty()) throw new IllegalArgumentException("TRANSLATION_BATCH_SIZE parameter is missing in configuration file.");

        String specificationXML = configMap.get("SPECIFICATION_XML");
        String specification2XML = configMap.get("SPECIFICATION_2XML");

        configMap.put("SOURCELANGUAGE", StringUtil.capitalize(sourceLanguage));
        configMap.put("TARGETLANGUAGE", StringUtil.capitalize(targetLanguage));
        try{
            Integer.parseInt(translationBatchSize);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("TRANSLATION_BATCH_SIZE parameter must be an integer.");
        }
        System.out.println(configMap);
    }

    public static String getValue(String key) {
        return configMap.get(key);
    }
    public static boolean containsKey(String key) {
        return configMap.containsKey(key);
    }
}

package org.example.Utils;

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

        validateConfigurationFile();
    }

    private static void validateConfigurationFile() {
        String sourceLanguage = configMap.get("SOURCE_LANGUAGE");
        if(sourceLanguage == null || sourceLanguage.isEmpty()) throw new IllegalArgumentException("SOURCE_LANGUAGE parameter is missing in configuration file.");
        String targetLanguage = configMap.get("TARGET_LANGUAGE");
        if(targetLanguage == null || targetLanguage.isEmpty()) throw new IllegalArgumentException("TARGET_LANGUAGE parameter is missing in configuration file.");
        String completionsUrl = configMap.get("COMPLETIONS_URL");
        if(completionsUrl == null || completionsUrl.isEmpty()) throw new IllegalArgumentException("COMPLETIONS_URL parameter is missing in configuration file.");
        String ttsEndpointUrl = configMap.get("TTS_URL");
        if(ttsEndpointUrl == null || ttsEndpointUrl.isEmpty()) throw new IllegalArgumentException("TTS_URL parameter is missing in configuration file.");
        String apiKey = configMap.get("API_KEY");
        if(apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("API_KEY parameter is missing in configuration file.");
        String model = configMap.get("MODEL");
        if(model == null || model.isEmpty()) throw new IllegalArgumentException("MODEL parameter is missing in configuration file.");
        String ttsModel = configMap.get("TTS_MODEL");
        if(ttsModel == null || ttsModel.isEmpty()) throw new IllegalArgumentException("TTS_MODEL parameter is missing in configuration file.");
        String ttsVoice = configMap.get("TTS_VOICE");
        if(ttsVoice == null || ttsVoice.isEmpty()) throw new IllegalArgumentException("TTS_VOICE parameter is missing in configuration file.");
        String translationsFolder = configMap.get("TRANSLATIONS_FOLDER");
        if(translationsFolder == null || translationsFolder.isEmpty()) throw new IllegalArgumentException("TRANSLATIONS_FOLDER parameter is missing in configuration file.");
        String translationBatchSize = configMap.get("TRANSLATION_BATCH_SIZE");
        if(translationBatchSize == null || translationBatchSize.isEmpty()) throw new IllegalArgumentException("TRANSLATION_BATCH_SIZE parameter is missing in configuration file.");
        //Sprint 3 comic from vignette
        String lessonTarget = configMap.get("LESSON_TARGET");
        if(lessonTarget == null || lessonTarget.isEmpty()) System.out.println("[WARNING] LESSON_TARGET parameter is missing in configuration file.");
        //Sprint 4 conjugation specification
        String conjugationSpec = configMap.get("CONJUGATION_XML");
        if(conjugationSpec == null || conjugationSpec.isEmpty()) System.out.println("[WARNING] CONJUGATION_XML parameter is missing in configuration file.");
        String conjugationTarget = configMap.get("CONJUGATION_TARGET");
        if(conjugationTarget == null || conjugationTarget.isEmpty()) System.out.println("[WARNING] CONJUGATION_TARGET parameter is missing in configuration file.");
        //Sprint 5 stories specification
        String storiesSpec = configMap.get("STORIES_XML");
        if(storiesSpec == null || storiesSpec.isEmpty()) System.out.println("[WARNING] STORIES_XML parameter is missing in configuration file.");
        String storiesTarget = configMap.get("STORIES_TARGET");
        if(storiesTarget == null || storiesTarget.isEmpty()) System.out.println("[WARNING] STORIES_TARGET parameter is missing in configuration file.");
        //Sprint 6
        String audioFolder = configMap.get("AUDIO_FOLDER");
        if(audioFolder == null || audioFolder.isEmpty()) System.out.println("[WARNING] AUDIO_FOLDER parameter is missing in configuration file.");
        String audioIndexPath = configMap.get("AUDIO_INDEX");
        if(audioIndexPath == null || audioIndexPath.isEmpty()) System.out.println("[WARNING] AUDIO_INDEX parameter is missing in configuration file.");
        String audioTarget = configMap.get("AUDIO_TARGET");
        if(audioTarget == null || audioTarget.isEmpty()) System.out.println("[WARNING] AUDIO_TARGET parameter is missing in configuration file.");

        //Format Languages
        configMap.put("SOURCE_LANGUAGE", StringUtil.capitalize(sourceLanguage));
        configMap.put("TARGET_LANGUAGE", StringUtil.capitalize(targetLanguage));
        try{
            Integer.parseInt(translationBatchSize);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("TRANSLATION_BATCH_SIZE parameter must be an integer.");
        }
    }

    public static String getValue(String key) {
        return configMap.get(key);
    }
    public static boolean containsKey(String key) {
        return configMap.containsKey(key) && configMap.get(key) != null && !configMap.get(key).isEmpty();
    }
}

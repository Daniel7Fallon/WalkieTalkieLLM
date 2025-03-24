package org.example;

import org.example.Assets.AssetMapFile;
import org.example.Assets.VignetteSchema;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialize(configFilePath);
        AssetMapFile.initialize();
        Dictionary.initialize();

        try {
            String sourceLanguage = ConfigurationFile.getValue("SOURCELANGUAGE");
            String targetLanguage = ConfigurationFile.getValue("TARGETLANGUAGE");

            System.out.println("\nTranslating first 5 vignettes");
            List<String> vignetteSchemaLeftTexts = new ArrayList<>();
            for(VignetteSchema vs : AssetMapFile.getVignetteSchemasInRange(0,5)) {
                vignetteSchemaLeftTexts.addAll(vs.getLeftTexts());
            }
            Translator.translateListAndWrite(vignetteSchemaLeftTexts);

            System.out.println("\nTranslating first 10 vignettes");
            vignetteSchemaLeftTexts = new ArrayList<>();
            for(VignetteSchema vs : AssetMapFile.getVignetteSchemasInRange(0,10)) {
                vignetteSchemaLeftTexts.addAll(vs.getLeftTexts());
            }
            Translator.translateListAndWrite(vignetteSchemaLeftTexts);
            System.out.println("\nDone");

         } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
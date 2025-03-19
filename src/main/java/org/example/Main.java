package org.example;

import org.example.Assets.AssetMapFile;
import org.example.Assets.Vignette;

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

        for(Vignette v: AssetMapFile.getVignettesInRange(0, 5)) {
            System.out.println(v);
        }
        try {
            String sourceLanguage = ConfigurationFile.getValue("SOURCELANGUAGE");
            String targetLanguage = ConfigurationFile.getValue("TARGETLANGUAGE");

            System.out.println("\nTranslating first 5 vignettes");
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 5));
            System.out.println("\nTranslating first 10 vignettes");
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 10));
            System.out.println("\nDone");

         } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
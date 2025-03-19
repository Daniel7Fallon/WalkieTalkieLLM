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

            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 5), sourceLanguage, targetLanguage);
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 10), sourceLanguage, targetLanguage);
            String test = "a charmer";
            System.out.println(test + " in " + ConfigurationFile.getValue("TARGETLANGUAGE").toLowerCase() + " is " + Dictionary.getTranslation(test, sourceLanguage, targetLanguage));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
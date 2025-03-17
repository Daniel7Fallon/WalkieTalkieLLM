package org.example;

import org.example.Assets.AssetMapFile;
import org.example.Assets.Vignette;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile> <pathToPosePairingWithBackgrounds.tsv> <pathToTranslations.txt>");
            System.out.println("Application will create a file if no file for the translation exists");
            return;
        }
        String configFilePath = args[0];
        ConfigurationFile.initialize(configFilePath);
        String assetMappingPath = args[1];
        AssetMapFile.initialize(assetMappingPath);
        String translationFilePath = args[2];

        for(Vignette v: AssetMapFile.getVignettesInRange(0, 5)) {
            System.out.println(v);
        }
        try {
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 5), translationFilePath);
            Translator.translateVignetteList(AssetMapFile.getVignettesInRange(0, 10), translationFilePath);
            String test = "a charmer";
            System.out.println(test + " in " + ConfigurationFile.getValue("TARGETLANGUAGE").toLowerCase() + " is " + Translator.getTranslation(test, translationFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
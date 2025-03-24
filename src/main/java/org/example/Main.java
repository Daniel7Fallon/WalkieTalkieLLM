package org.example;

import org.example.Assets.VignetteManager;
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
        VignetteManager.initialize();
        Dictionary.initialize();

        try {
            System.out.println("\nTranslating first 5 vignettes");
            Translator.translateListAndWrite(VignetteManager.getLeftTextsInVignetteSchemasInRange(0, 5));

            System.out.println("\nTranslating first 10 vignettes");
            Translator.translateListAndWrite(VignetteManager.getLeftTextsInVignetteSchemasInRange(0, 10));

            System.out.println("\nDone");

         } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
package org.example;

import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.Comic.XMLGenerator;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        try {
            ConfigurationFile.initialize(configFilePath);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid config file: " + e.getMessage());
            return;
        }
        VignetteManager.initialize();
        Dictionary.initialize();

        try {
            System.out.println("\nTranslating first 5 vignettes");
            Translator.translateVignetteSchemasInRange(0,5);

            System.out.println("\nTranslating first 10 vignettes");
            Translator.translateVignetteSchemasInRange(0,10);

            System.out.println("\nTranslating first 100 vignettes");
            Translator.translateVignetteSchemasInRange(0,100);

        } catch (Exception e) {
            e.printStackTrace();
        }

        VignetteSchema firstVS = VignetteManager.getVignetteSchemas().getFirst();
        System.out.println("First VignetteSchema: " + firstVS);
        for(int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". " + firstVS.getRandVignette());
        }

        XMLGenerator.generateXML(XMLGenerator.createDocument());

    }
}
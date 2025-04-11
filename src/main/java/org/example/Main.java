package org.example;

import org.example.Comic.*;
import org.example.XML.VignetteToComic;
import org.jdom2.JDOMException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.XML.XMLParser;
import org.example.XML.XMLGenerator;

import static org.example.Utils.StringUtil.removePluralIdentifier;


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

        //Sprint 2 task
        try {
            System.out.println("\nTranslating first 5 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,5);

            System.out.println("\nTranslating first 10 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,10);

            System.out.println("\nTranslating first 20 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,20);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sprint 3 tasks
        VignetteSchema firstVS = VignetteManager.getVignetteSchemas().getFirst();
        System.out.println("First VignetteSchema: " + firstVS);
        for(int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". " + firstVS.getRandVignette());
        }
        ArrayList<Figure> figures = new ArrayList<Figure>();
        Figure leftFigure = new Figure();
        leftFigure.setName("Daniel");
        leftFigure.setSkin("Brown");

        Figure rightFigure = new Figure();
        rightFigure.setName("Harry");
        rightFigure.setSkin("White");

        figures.add(leftFigure);
        figures.add(rightFigure);

        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(0, 10);
        Comic comic = VignetteToComic.createComicFromVignette(figures, vignetteSchemas);
        XMLGenerator.generateXMLFromComic(comic, ConfigurationFile.getValue("LESSON_TARGET"));

        // Sprint 4 task
        String conjugationSpec = ConfigurationFile.getValue("CONJUGATION_XML");
        String conjugationTarget = ConfigurationFile.getValue("CONJUGATION_TARGET");
        XMLGenerator.generateBilingualXML(conjugationSpec, conjugationTarget);

        /* ---Sprint 5 task---
         * x Parse stories spec file into Comic
         * x Take 10 random scenes to generate audiovisual descriptions
         * o Parse AI response for dialogue and captions
         * o Rebuild scenes and add to new Comic
         * o Write new comic to XML file
         */
        StoryManager.generateRandomStories();



    }


}
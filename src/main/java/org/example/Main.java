package org.example;

import org.example.Comic.*;
import org.example.Orchestration.Orchestrator;
import org.example.Translation.Dictionary;
import org.example.Utils.ConfigurationFile;

import java.io.IOException;
import java.util.List;

import org.example.Vignette.VignetteManager;
import org.example.XML.XMLGenerator;
import org.example.XML.XMLParser;
import org.jdom2.JDOMException;


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

        final String CONJUGATION_XML = "conjugation.xml";
        final String STORIES_XML = "stories.xml";
        final List<String> LESSON_SCHEDULE = List.of(ConfigurationFile.getValue("LESSON_SCHEDULE").split(" "));
        final String LESSON_TARGET = ConfigurationFile.getValue("LESSON_TARGET");

        Comic storiesComic = null;
        try {
            storiesComic = XMLParser.parseComicFromResourcesPath(STORIES_XML);
        } catch (IOException | JDOMException e) {
            System.out.println("Error parsing stories comic: " + e.getMessage());
        }

        // Sprint 3 tasks
        //LESSON_TARGET is now used for sprint 7
        /*
        if(ConfigurationFile.containsKey("LESSON_TARGET")) {
            ArrayList<Figure> figures = new ArrayList<>();
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
        } else {
            System.out.println("Skipping generation of lesson from vignette schemas (Sprint 3)");
        }
        */

        //Sprint 7
        int i = 0;
        Comic finalComic = new Comic();
        for(String s : LESSON_SCHEDULE) {
            System.out.println((++i) + ". " + s);
            if(s.equals("conjugation")) {
                try {
                    Comic conjugationComic = XMLParser.parseComicFromResourcesPath(CONJUGATION_XML);
                    conjugationComic = ComicPostProcessor.generateBilingualComic(conjugationComic);
                    finalComic.appendComic(conjugationComic);
                } catch (IOException | JDOMException e) {
                    System.out.println("Creation of Conjugation Section failed: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (s.equals("left")) {
                //Create comic using only left figure and left text
                //Where left figure only speaks, and repeats translation in the next panel
                //Append this comic to final comic
            } else if(s.equals("whole")) {
                //Create comic using left and right figures, and combined text
                //Where left figure speaks source language, and right speaks target language
                //No need to split panels yet
                //Append this comic to final comic
            } else if(s.equals("story")) {
                if(storiesComic == null) {
                    System.out.println("Stories comic is null!");
                    continue;
                }
                try {
                    Comic aiDialogueComic = Orchestrator.generateRandomStoriesComic(storiesComic, 1);
                    Comic bilingualStoriesComic = ComicPostProcessor.generateBilingualComic(aiDialogueComic);
                    finalComic.appendComic(bilingualStoriesComic);
                } catch (IOException | JDOMException e) {
                    System.out.println("Creation of Stories comic failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        finalComic.splitAllMultiDialoguePanels();

        try {
            finalComic.addAudio();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error adding audio: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            XMLGenerator.generateXMLFromComic(finalComic, LESSON_TARGET);
        } catch (IOException e) {
            System.out.println("Error generating XML: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
package org.example;

import org.example.Audio.AudioManager;
import org.example.Comic.*;
import org.example.Story.StoryManager;
import org.example.Translation.Dictionary;
import org.example.Translation.Translator;
import org.example.Utils.ConfigurationFile;
import org.example.XML.VignetteToComic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.XML.XMLGenerator;
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
        if(ConfigurationFile.containsKey("LESSON_TARGET")) {
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
        } else {
            System.out.println("Skipping generation of lesson from vignette schemas (Sprint 3)");
        }

        // Sprint 4 task
        if(ConfigurationFile.containsKey("CONJUGATION_XML") && ConfigurationFile.containsKey("CONJUGATION_TARGET")) {
            String conjugationSpec = ConfigurationFile.getValue("CONJUGATION_XML");
            String conjugationTarget = ConfigurationFile.getValue("CONJUGATION_TARGET");
            XMLGenerator.generateBilingualXML(conjugationSpec, conjugationTarget);
        } else {
            System.out.println("Skipping generation of conjugation lesson (Sprint 4)");
        }

        //Sprint 5 task
        if(ConfigurationFile.containsKey("STORIES_XML") && ConfigurationFile.containsKey("STORIES_TARGET") ) {
            try {
                Comic finalComic = StoryManager.generateRandomStoriesComic(10);
                XMLGenerator.generateXMLFromComic(finalComic, ConfigurationFile.getValue("STORIES_TARGET"));
            } catch (IOException | JDOMException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Skipping generation of random stories (Sprint 5)");
        }

        // Sprint 6 task
        if(ConfigurationFile.containsKey("STORIES_XML") && ConfigurationFile.containsKey("AUDIO_FOLDER")
                && ConfigurationFile.containsKey("AUDIO_INDEX") && ConfigurationFile.containsKey("AUDIO_TARGET")) {

           try {
               Comic singleComic = StoryManager.generateRandomStoriesComic(1);
               //Testing split and adding audio
               XMLGenerator.generateXMLFromComic(singleComic, "test.xml");
               singleComic.splitAllMultiDialoguePanels();
               XMLGenerator.generateXMLFromComic(singleComic, "test_output.xml");
               try {
                   AudioManager.addAudio(singleComic);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               XMLGenerator.generateXMLFromComic(singleComic, "test_audio_output.xml");
           } catch (Exception e) {
               System.out.println(e);
           }

        } else {
            System.out.println("Skipping generation of audio story (Sprint 6)");
        }




    }


}
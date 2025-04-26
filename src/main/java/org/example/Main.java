package org.example;

import org.example.Comic.*;
import org.example.Story.StoryGenerator;
import org.example.Translation.Dictionary;
import org.example.Utils.ConfigurationFile;

import java.io.IOException;
import java.util.List;

import org.example.Vignette.VignetteManager;
import org.example.Vignette.VignetteToComic;
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

        Comic conjugationComic = null;
        try {
            conjugationComic = XMLParser.parseComicFromResourcesPath(CONJUGATION_XML);
        } catch (IOException | JDOMException e) {
            System.out.println("Error parsing conjugation comic: " + e.getMessage());
        }

        Comic storiesComic = null;
        try {
            storiesComic = XMLParser.parseComicFromResourcesPath(STORIES_XML);
        } catch (IOException | JDOMException e) {
            System.out.println("Error parsing stories comic: " + e.getMessage());
        }

        //Translate all vignettes
        if(ConfigurationFile.getValue("TRANSLATE_ALL_VIGNETTES").equals("true")) {
            System.out.println("Translating all vignette schemas.");
            try {
                VignetteManager.translateAllVignetteSchemas();
            } catch (IOException e) {
                System.out.println("Error translating all vignette schemas: " + e.getMessage());
                e.printStackTrace();
            }
        } else if(ConfigurationFile.getValue("TRANSLATE_ALL_VIGNETTES").equals("false")) {
            System.out.println("Skipping translation of all vignette schemas.");
        }

        //Figures for vignette comics
        Figure leftFigure = new Figure();
        leftFigure.setId("Daniel");
        leftFigure.setName("Daniel");
        leftFigure.setSkin("white");
        leftFigure.setFacing("right");

        Figure rightFigure = new Figure();
        rightFigure.setId("Harry");
        rightFigure.setName("Harry");
        rightFigure.setSkin("white");
        rightFigure.setFacing("left");

        //Lesson construction
        int i = 0;
        Comic finalComic = new Comic();
        for(String s : LESSON_SCHEDULE) {
            //Adding conjugation section
            if(s.equals("conjugation")) {
                System.out.println("Adding verb conjugation section.");
                if(conjugationComic == null) {
                    System.out.println("Conjugation Comic is null!");
                    continue;
                }
                Comic verbComic = new Comic();
                verbComic.addAllFigures(conjugationComic.getFigures());
                verbComic.addAllScenes(conjugationComic.getRandomScenes(1));
                verbComic.addAllFigures(conjugationComic.getFigures());

                if(verbComic.removeFirstPanel()) {
                    verbComic.addSectionPanel(++i, "Verb Conjugation");

                    try {
                        verbComic = ComicPostProcessor.generateBilingualComic(verbComic);
                    } catch (IOException e) {
                        System.out.println("Creation of Bilingual verb comic failed: " + e.getMessage());
                        e.printStackTrace();
                    }

                    finalComic.appendComic(verbComic);

                    System.out.println("Successfully added conjugation section");
                } else {
                    System.out.println("Failed to remove verb conjugation comic's first panel. Scenes size = " + verbComic.getScenes().size());
                }

            //Adding left vignette section
            } else if (s.equals("left")) {
                Comic leftComic = new Comic();
                try {
                    leftComic = VignetteToComic.createLeftVignetteComic(leftFigure);
                } catch (IOException e) {
                    System.out.println("Error creating left vignette comic: " + e.getMessage());
                    e.printStackTrace();
                }
                if(leftComic != null) {
                    leftComic.addSectionPanel(++i, "Simple Vocabulary");
                    finalComic.appendComic(leftComic);
                    System.out.println("Successfully added left section");
                } else {
                    System.out.println("Left Vignette Comic is null.");
                }

            //Adding whole vignette section
            } else if(s.equals("whole")) {
                Comic wholeComic = new Comic();
                try {
                    wholeComic = VignetteToComic.createWholeVignetteComic(leftFigure, rightFigure);
                } catch (IOException e) {
                    System.out.println("Error creating whole vignette comic: " + e.getMessage());
                    e.printStackTrace();
                }
                if(wholeComic != null) {
                    wholeComic.addSectionPanel(++i, "Vocabulary");
                    finalComic.appendComic(wholeComic);
                    System.out.println("Successfully added whole section");
                } else {
                    System.out.println("Whole Vignette Comic is null.");
                }

            //Adding mini-story section
            } else if(s.equals("story")) {
                System.out.println("Adding mini-story section.");
                if(storiesComic == null) {
                    System.out.println("Stories comic is null!");
                    continue;
                }

                Comic storyComic = StoryGenerator.generateRandomStoriesComic(storiesComic, 1);
                if(storyComic.removeFirstPanel()) {
                    storyComic.addSectionPanel(++i, "Mini-Story");

                    try {
                        storyComic = ComicPostProcessor.generateBilingualComic(storyComic);
                    } catch (IOException e) {
                        System.out.println("Creation of Bilingual mini-story comic failed: " + e.getMessage());
                        e.printStackTrace();
                    }


                    finalComic.appendComic(storyComic);
                    System.out.println("Successfully added mini-story section");
                } else {
                    System.out.println("Failed to remove mini-story comic's first panel. Scenes size = " + storyComic.getScenes().size());
                }

            //Improper lesson schedule argument encountered
            } else {
                System.out.println("Invalid lesson schedule argument: " + s);
            }
        }

        //Interleave Translated Panels

        finalComic.splitAllMultiDialoguePanels();
        System.out.println("Split all multi dialogue panels");

        /* Commented out for speed of testing
        try {
            finalComic.addAudio();
            System.out.println("Added audio successfully");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error adding audio: " + e.getMessage());
            e.printStackTrace();
        }

         */

        try {
            XMLGenerator.generateXMLFromComic(finalComic, LESSON_TARGET);
        } catch (IOException e) {
            System.out.println("Error generating XML: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
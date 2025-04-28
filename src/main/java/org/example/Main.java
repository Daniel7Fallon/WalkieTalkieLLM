package org.example;

import org.example.Comic.*;
import org.example.Lesson.LessonSectionBuilder;
import org.example.Translation.Dictionary;
import org.example.Utils.ConfigurationFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.example.Vignette.VignetteManager;
import org.example.XML.XMLGenerator;
import org.example.XML.XMLParser;
import org.jdom2.JDOMException;

public class Main {

    // Lesson Section Types
    private enum LessonSectionType {
        CONJUGATION("conjugation"),
        LEFT_VIGNETTE("left"),
        WHOLE_VIGNETTE("whole"),
        STORY("story");

        private final String configValue;

        LessonSectionType(String configValue) {
            this.configValue = configValue;
        }

        public static LessonSectionType fromString(String text) {
            for (LessonSectionType b : LessonSectionType.values()) {
                if (b.configValue.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            System.err.println("WARN: Unknown lesson section type in config: " + text);
            return null;
        }
    }

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];

        // Initialisation
        try {
            ConfigurationFile.initialize(configFilePath);
            System.out.println("INFO: Loaded configuration file from: " + configFilePath);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid or unreadable config file: " + e.getMessage());
            e.printStackTrace(System.err);
            return;
        }

        VignetteManager.initialize();
        Dictionary.initialize();
        System.out.println("INFO: Loaded VignetteManager and Dictionary.");

        // Configuration Values
        final String CONJUGATION_XML = "conjugation.xml"; // Not setting this to be configurable because the stuff is in the resources folder.
        final String STORIES_XML = "stories.xml";
        final String LESSON_TARGET = ConfigurationFile.getValue("LESSON_TARGET");
        final boolean TRANSLATE_ALL_VIGNETTES = Boolean.parseBoolean(ConfigurationFile.getValue("TRANSLATE_ALL_VIGNETTES"));
        final List<LessonSectionType> LESSON_SCHEDULE = Arrays.stream(ConfigurationFile.getValue("LESSON_SCHEDULE").split(" "))
                .map(LessonSectionType::fromString)
                .filter(Objects::nonNull) // Filtering out any unknown types
                .toList();

        if (CONJUGATION_XML == null || STORIES_XML == null || LESSON_TARGET == null || LESSON_SCHEDULE.isEmpty()) {
            System.err.println("ERROR: Missing required configuration values (Paths, Target, or Schedule). Check config file.");
            return;
        }

        // Figures for vignette comics
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

        // Load Base Comics
        Comic conjugationComic = loadComicResource(CONJUGATION_XML, "conjugation");
        Comic storiesComic = loadComicResource(STORIES_XML, "stories");

        // Pre-Translation Option
        handleVignetteTranslation(TRANSLATE_ALL_VIGNETTES);

        // Lesson construction
        Comic finalComic = new Comic();
        int sectionNumber = 0;
        System.out.println("INFO: Starting lesson construction based on schedule: " + LESSON_SCHEDULE);

        for (LessonSectionType sectionType : LESSON_SCHEDULE) {
            Comic sectionComic = null;
            sectionNumber++;

            switch (sectionType) {
                case CONJUGATION:
                    sectionComic = LessonSectionBuilder.createConjugationSection(conjugationComic, sectionNumber);
                    break;
                case LEFT_VIGNETTE:
                    sectionComic = LessonSectionBuilder.createLeftVignetteSection(leftFigure, sectionNumber);
                    break;
                case WHOLE_VIGNETTE:
                    sectionComic = LessonSectionBuilder.createWholeVignetteSection(leftFigure, rightFigure, sectionNumber);
                    break;
                case STORY:
                    sectionComic = LessonSectionBuilder.createStorySection(storiesComic, sectionNumber);
                    break;
                // No default needed due to pre-filtering
            }

            if (sectionComic != null) {
                finalComic.appendComic(sectionComic);
                System.out.println("INFO: Successfully added section " + sectionNumber + ": " + sectionType);
            } else {
                System.err.println("WARN: Skipping section " + sectionNumber + " (" + sectionType + ") due to creation failure.");
            }
        }

        if (finalComic.getScenes().isEmpty()) {
            System.err.println("ERROR: Failed to create any lesson sections. Final comic is empty. Aborting.");
            return;
        }

        // Post-Processing
        System.out.println("INFO: Starting post-processing...");
        finalComic.splitAllMultiDialoguePanels();
        System.out.println("INFO: Split multi-dialogue panels.");

        try {
            finalComic.addAudio();
            System.out.println("INFO: Audio added successfully.");
        } catch (IOException | InterruptedException e) {
            System.err.println("ERROR: Error adding audio to the final comic: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        // Final Output
        System.out.println("INFO: Generating final XML output to target: " + LESSON_TARGET);
        try {
            XMLGenerator.generateXMLFromComic(finalComic, LESSON_TARGET);
            System.out.println("INFO: Successfully generated XML: " + LESSON_TARGET);
        } catch (IOException e) {
            System.err.println("ERROR: Error generating final XML '" + LESSON_TARGET + "': " + e.getMessage());
            e.printStackTrace(System.err);
        }

        System.out.println("INFO: Lesson generation process finished.");
    }

    /**
     * Handles the optional translation of all vignette schemas.
     * @param translate Flag from configuration.
     */
    private static void handleVignetteTranslation(boolean translate) {
        if (translate) {
            System.out.println("INFO: Translating all vignette schemas as per configuration.");
            try {
                VignetteManager.translateAllVignetteSchemas();
                System.out.println("INFO: Finished translating vignette schemas.");
            } catch (IOException e) {
                System.err.println("ERROR: Error translating all vignette schemas: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        } else {
            System.out.println("INFO: Skipping translation of all vignette schemas.");
        }
    }

    /**
     * Loads a Comic object from a resource path.
     * @param resourcePath Path to the XML file within resources.
     * @param comicType Descriptive name for logging (e.g., "conjugation", "stories").
     * @return Loaded Comic object, or null if loading fails.
     */
    private static Comic loadComicResource(String resourcePath, String comicType) {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            System.err.println("ERROR: Resource path for " + comicType + " comic is missing in configuration.");
            return null;
        }
        try {
            System.out.println("INFO: Loading " + comicType + " comic from: " + resourcePath);
            Comic comic = XMLParser.parseComicFromResourcesPath(resourcePath);
            System.out.println("INFO: Successfully loaded " + comicType + " comic.");
            return comic;
        } catch (IOException | JDOMException e) {
            System.err.println("ERROR: Error parsing " + comicType + " comic from '" + resourcePath + "': " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }
}
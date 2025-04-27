package org.example.Lesson;

import org.example.Comic.*;
import org.example.Comic.Dialogue.SceneDialogue;
import org.example.Vignette.VignetteToComic;

import java.io.IOException;
import java.util.List;

public class LessonSectionBuilder {

    /**
     * Generates a new Comic containing a specified number of randomly selected scenes from stories.xml
     * @param originalComic The comic to pull scenes and figures from
     * @param numberOfStories The number of random scenes to select.
     * @return The new comic containing the randomly selected scenes.
     */
    private static Comic generateRandomStoriesComic(Comic originalComic, int numberOfStories) {
        List<Scene> scenes = originalComic.getRandomScenes(numberOfStories);

        Comic outputComic = new Comic();
        outputComic.addAllFigures(originalComic.getFigures());
        outputComic.addAllScenes(scenes);

        List<SceneDialogue> sceneDialogues = outputComic.generateDialogueFromAudioDescriptionComic();

        outputComic.removeAllAboveAndBelow();
        outputComic.replaceDialogue(sceneDialogues);

        return outputComic;
    }

    /**
     * Creates the Conjugation section comic.
     * @param baseComic The pre-loaded conjugation comic template.
     * @param sectionNumber The current section number.
     * @return The generated Comic section, or null on failure.
     */
    public static Comic createConjugationSection(Comic baseComic, int sectionNumber) {
        // Using System.out for DEBUG level info
        System.out.println("DEBUG: Attempting to create conjugation section " + sectionNumber + ".");
        if (baseComic == null) {
            System.err.println("ERROR: Cannot create conjugation section " + sectionNumber + ": Base conjugation comic was not loaded.");
            return null;
        }

        Comic sectionComic = new Comic();
        List<Scene> randomScenes = baseComic.getRandomScenes(1);
        if (randomScenes == null || randomScenes.isEmpty()) {
            System.err.println("ERROR: Failed to get random scene from base conjugation comic for section " + sectionNumber + ".");
            return null;
        }
        sectionComic.addAllFigures(baseComic.getFigures());
        sectionComic.addAllScenes(randomScenes);

        if (sectionComic.removeFirstPanel()) {
            sectionComic.addSectionPanel(sectionNumber, "Verb Conjugation");
            try {
                sectionComic = ComicPostProcessor.generateBilingualComic(sectionComic);
                System.out.println("DEBUG: Successfully created bilingual conjugation section " + sectionNumber + ".");
                return sectionComic;
            } catch (IOException e) {
                System.err.println("ERROR: Failed to make conjugation section " + sectionNumber + " bilingual: " + e.getMessage());
                e.printStackTrace(System.err);
                return null;
            }
        } else {
            System.err.println("ERROR: Failed to prepare conjugation section " + sectionNumber + ": Could not remove first panel. Scenes size = " + sectionComic.getScenes().size());
            return null;
        }
    }

    /**
     * Creates the Left Vignette section comic.
     * @param figure The figure to use.
     * @param sectionNumber The current section number.
     * @return The generated Comic section, or null on failure.
     */
    public static Comic createLeftVignetteSection(Figure figure, int sectionNumber) {
        System.out.println("DEBUG: Attempting to create left vignette section " + sectionNumber + ".");
        try {
            Comic sectionComic = VignetteToComic.createLeftVignetteComic(figure);
            if (sectionComic != null) {
                sectionComic.addSectionPanel(sectionNumber, "Simple Vocabulary");
                System.out.println("DEBUG: Successfully created left vignette section " + sectionNumber + ".");
                return sectionComic;
            } else {
                System.err.println("ERROR: Left Vignette Comic creation returned null for section " + sectionNumber + ".");
                return null;
            }
        } catch (IOException e) {
            System.err.println("ERROR: Error creating left vignette section " + sectionNumber + ": " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Creates the Whole Vignette section comic.
     * @param leftFigure The figure on the left.
     * @param rightFigure The figure on the right.
     * @param sectionNumber The current section number.
     * @return The generated Comic section, or null on failure.
     */
    public static Comic createWholeVignetteSection(Figure leftFigure, Figure rightFigure, int sectionNumber) {
        System.out.println("DEBUG: Attempting to create whole vignette section " + sectionNumber + ".");
        try {
            Comic sectionComic = VignetteToComic.createWholeVignetteComic(leftFigure, rightFigure);
            if (sectionComic != null) {
                sectionComic.addSectionPanel(sectionNumber, "Vocabulary");
                System.out.println("DEBUG: Successfully created whole vignette section " + sectionNumber + ".");
                return sectionComic;
            } else {
                System.err.println("ERROR: Whole Vignette Comic creation returned null for section " + sectionNumber + ".");
                return null;
            }
        } catch (IOException e) {
            System.err.println("ERROR: Error creating whole vignette section " + sectionNumber + ": " + e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Creates the Story section comic.
     * @param baseComic The pre-loaded stories comic template.
     * @param sectionNumber The current section number.
     * @return The generated Comic section, or null on failure.
     */
    public static Comic createStorySection(Comic baseComic, int sectionNumber) {
        System.out.println("DEBUG: Attempting to create story section " + sectionNumber + ".");
        if (baseComic == null) {
            System.err.println("ERROR: Cannot create story section " + sectionNumber + ": Base stories comic was not loaded.");
            return null;
        }

        Comic sectionComic = LessonSectionBuilder.generateRandomStoriesComic(baseComic, 1);
        if (sectionComic == null || sectionComic.getScenes().isEmpty()){
            System.err.println("ERROR: Story generator failed to produce scenes for section " + sectionNumber + ".");
            return null;
        }

        if (sectionComic.removeFirstPanel()) {
            sectionComic.addSectionPanel(sectionNumber, "Mini-Story");
            try {
                sectionComic = ComicPostProcessor.generateBilingualComic(sectionComic);
                System.out.println("DEBUG: Successfully created bilingual story section " + sectionNumber + ".");
                return sectionComic;
            } catch (IOException e) {
                System.err.println("ERROR: Failed to make story section " + sectionNumber + " bilingual: " + e.getMessage());
                e.printStackTrace(System.err);
                return null;
            }
        } else {
            System.err.println("ERROR: Failed to prepare story section " + sectionNumber + ": Could not remove first panel. Scenes size = " + sectionComic.getScenes().size());
            return null;
        }
    }

}

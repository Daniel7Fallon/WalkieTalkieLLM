package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Translation.Dictionary;
import org.example.Translation.Translator;

public class ComicPostProcessor {

    /* Translates comic dialogue
     * Returns new comic with translated panels interleaved
     */
    public static Comic generateBilingualComic(Comic comic) throws IOException{
        Translator.batchTranslateList(comic.getAllBalloonContent());
        return ComicPostProcessor.addTranslationPanels(comic);
    }

    /* Takes a comic that has it's dialogues already translated
     * Returns new comic with translated panels interleaved
     */
    private static Comic addTranslationPanels(Comic originalComic) {
        Comic newComic = new Comic();
        List<Scene> newScenes = new ArrayList<>();
        for (Scene scene : originalComic.getScenes()) {

            List<Panel> newPanels = new ArrayList<>();
            for (Panel originalPanel : scene.getPanels()) {
                newPanels.add(originalPanel.deepCopy());

                // Create translated version if translatable
                if (originalPanel.hasBalloonContent()) {
                    Panel translatedPanel = createTranslatedPanel(originalPanel);
                    newPanels.add(translatedPanel);
                }
            }

            Scene newScene = new Scene();
            newScene.addAllPanels(newPanels);
            newScenes.add(newScene);
        }
        newComic.addAllScenes(newScenes);
        newComic.removePluralIdentifiers();
        return newComic;
    }

    private static Panel createTranslatedPanel(Panel original) {
        Panel translated = new Panel();
        translated.setSetting(original.getSetting());
        translated.setBorder(original.getBorder());
        translated.setBelow(original.getBelow());

        // Map original side to translated sides
        if (original.hasLeft()) translated.setLeftSide(createTranslatedSide(original.getLeftSide()));
        if (original.hasMiddle()) translated.setMiddleSide(createTranslatedSide(original.getMiddleSide()));
        if (original.hasRight()) translated.setRightSide(createTranslatedSide(original.getRightSide()));

        return translated;
    }

    private static PanelSide createTranslatedSide(PanelSide originalPanelSide) {
        PanelSide translatedPanelSide = new PanelSide();
        translatedPanelSide.setPanelFigure(originalPanelSide.getPanelFigure());

        if (originalPanelSide.hasBalloonContent()) {
            try {
                String[] translations = Dictionary.getSourceAndTargetTranslations(
                        originalPanelSide.getBalloonContent()
                );
                translatedPanelSide.setBalloonContent(translations[1]);
                translatedPanelSide.setBalloonStatus("speaking");
            } catch (IOException | NullPointerException e) {
                translatedPanelSide.setBalloonContent("[TRANSLATION FAILED]");
                translatedPanelSide.setBalloonStatus("error");
            }
        }
        return translatedPanelSide;
    }
}
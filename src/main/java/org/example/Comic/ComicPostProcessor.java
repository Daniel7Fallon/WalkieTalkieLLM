package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Translation.Dictionary;
import org.example.Translation.Translator;

public class ComicPostProcessor {

    /**
     * Translates the dialogue within a given comic and generates a new comic
     * where each original panel with dialogue is followed by its translated counterpart.
     *
     * @param comic The original Comic object whose dialogue needs translation.
     * @return A new Comic object containing the original panels interleaved with
     * their corresponding translated panels. Figures are copied from the original.
     * @throws IOException if the batch translation process encounters an I/O error.
     */
    public static Comic generateBilingualComic(Comic comic) throws IOException{
        Translator.batchTranslateList(comic.getAllBalloonContent());
        return ComicPostProcessor.addTranslationPanels(comic);
    }

    /**
     * Creates a new comic by interleaving translated panels after original panels
     * that contain dialogue.
     *
     * @param originalComic The Comic object containing original panels.
     * @return A new Comic object with original and translated panels interleaved.
     * Figures are copied from the original comic.
     */
    private static Comic addTranslationPanels(Comic originalComic) {
        Comic newComic = new Comic();
        newComic.addAllFigures(originalComic.getFigures());
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

    /**
     * Creates a translated version of a single Panel based on an original Panel.
     *
     * @param original The original Panel object to translate.
     * @return A new Panel object representing the translated version of the original.
     * Contains translated dialogue where applicable.
     */
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

    /**
     * Creates a translated version of a PanelSide based on an original PanelSide.
     *
     * @param originalPanelSide The original PanelSide object containing the figure
     * and potentially dialogue to be translated.
     * @return A new PanelSide object with the figure copied and balloon content
     * set to the translation or an error message.
     */
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
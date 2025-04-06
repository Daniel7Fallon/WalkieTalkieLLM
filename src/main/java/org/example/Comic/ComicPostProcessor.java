package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.example.Dictionary;
import static org.example.Utils.StringUtil.removePluralIdentifier;

public class ComicPostProcessor {
    public static void addTranslationPanels(Comic comic) {
        for (Scene scene : new ArrayList<>(comic.getScenes())) {
            List<Panel> newPanels = new ArrayList<>();

            for (Panel original : scene.getPanels()) {
                // Keep original panel
                newPanels.add(original);

                // Create translated version if translatable
                if (hasTranslatableContent(original)) {
                    try {
                        Panel translated = createTranslatedPanel(original);
                        if (translated != null) {
                            newPanels.add(translated);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            scene.getPanels().clear();
            scene.getPanels().addAll(newPanels);
        }
    }

    private static boolean hasTranslatableContent(Panel panel) {
        return Stream.of(panel.getLeftSide(), panel.getMiddleSide(), panel.getRightSide())
                .anyMatch(side -> side != null && side.getBalloonContent() != null);
    }

    private static Panel createTranslatedPanel(Panel original) {
        Panel translated = new Panel();
        translated.setSetting(original.getSetting());
        translated.setBorder(original.getBorder());
        translated.setBelow(original.getBelow());

        // Map original side to translated sides
        if (original.getLeftSide() != null) {
            translated.setRightSide(translateSide(original.getLeftSide()));
        }

        if (original.getMiddleSide() != null) {
            translated.setMiddleSide(translateSide(original.getMiddleSide()));
        }

        if (original.getRightSide() != null) {
            translated.setLeftSide(translateSide(original.getRightSide()));
        }

        return translated;
    }

    private static PanelSide translateSide(PanelSide original) {
        PanelSide translated = new PanelSide();
        translated.setPanelFigure(original.getPanelFigure());

        if (original.getBalloonContent() != null) {
            try {
                String[] translations = Dictionary.getSourceAndTargetTranslations(
                        original.getBalloonContent()
                );
                translated.setBalloonContent(translations[1]);
                translated.setBallonStatus("speaking");
            } catch (IOException | NullPointerException e) {
                translated.setBalloonContent("[TRANSLATION MISSING]");
                translated.setBallonStatus("error");
            }
        }

        return translated;
    }
}
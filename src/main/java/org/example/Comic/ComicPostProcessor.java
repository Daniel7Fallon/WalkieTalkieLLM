package org.example.Comic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.example.Dictionary;

public class ComicPostProcessor {

    public static void addTranslationPanels(Comic comic) {
        for (Scene scene : comic.getScenes()) {
            List<Panel> originalPanels = new ArrayList<>(scene.getPanels());

            for (int i = 0; i < originalPanels.size(); i++) {
                Panel original = originalPanels.get(i);

                if (original.getLeftSide() != null && "speaking".equals(original.getLeftSide().getBalloonStatus())) {
                    String originalText = original.getLeftSide().getBalloonContent();
                    String[] sourceAndTarget;
                    try {
                        sourceAndTarget = Dictionary.getSourceAndTargetTranslations(originalText);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                    if (sourceAndTarget == null) continue;

                    String translatedText = sourceAndTarget[1];

                    Panel translatedPanel = new Panel();
                    translatedPanel.setSetting(original.getSetting());
                    translatedPanel.setBorder(original.getBorder());
                    translatedPanel.setBelow(original.getBelow());

                    if (original.getLeftSide() != null) {
                        PanelSide left = new PanelSide();
                        left.setPanelFigure(original.getLeftSide().getPanelFigure());
                        translatedPanel.setLeftSide(left);
                    }

                    if (original.getRightSide() != null) {
                        PanelSide right = new PanelSide();
                        right.setPanelFigure(original.getRightSide().getPanelFigure());
                        right.setBallonStatus("speaking");
                        right.setBalloonContent(translatedText);
                        translatedPanel.setRightSide(right);
                    }

                    scene.addPanelAtIndex(i + 1, translatedPanel);
                    i++; // This is so that the loop is i + 2 rather than i + 1
                }
            }
        }
    }
}

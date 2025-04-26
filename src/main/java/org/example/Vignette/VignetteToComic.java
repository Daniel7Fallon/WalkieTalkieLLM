package org.example.Vignette;

import org.example.Comic.*;
import org.example.Translation.Dictionary;
import org.example.Translation.Translator;

import java.io.IOException;
import java.util.List;

public class VignetteToComic {

    //Returns a comic with two characters speaking the source and target for the combined text of a vignette respectively
    public static Comic createWholeVignetteComic(Figure leftFigure, Figure rightFigure) throws IOException {
        VignetteSchema vignetteSchema = null;
        while(vignetteSchema == null || vignetteSchema.getCombinedTexts().isEmpty()) {
            vignetteSchema = VignetteManager.getRandomVignetteSchema();
        }
        Vignette vignette = vignetteSchema.getRandomVignette();
        Comic comic = new Comic();
        comic.addFigure(leftFigure);
        comic.addFigure(rightFigure);
        comic.addScene(createWholeVignetteScene(vignette, leftFigure, rightFigure));
        return comic;
    }

    private static Scene createWholeVignetteScene(Vignette vignette, Figure leftFigure, Figure rightFigure) throws IOException {
        Scene scene = new Scene();
        String combinedText = vignette.getCombinedText();
        Translator.batchTranslateList(List.of(combinedText));
        String[] translations = Dictionary.getSourceAndTargetTranslations(combinedText);

        Panel panel = new Panel();
        panel.setSetting(vignette.getBackgrounds());
        //Left
        PanelSide leftSide = new PanelSide();
        PanelFigure leftPanelFigure = new PanelFigure(leftFigure);
        leftPanelFigure.setPose(vignette.getLeftPose());
        leftSide.setPanelFigure(leftPanelFigure);
        leftSide.setBalloonStatus("Speech");
        leftSide.setBalloonContent(translations[0]);
        panel.setLeftSide(leftSide);
        //Right
        PanelSide rightSide = new PanelSide();
        PanelFigure rightPanelFigure = new PanelFigure(rightFigure);
        rightPanelFigure.setPose(vignette.getRightPose());
        rightSide.setPanelFigure(rightPanelFigure);
        rightSide.setBalloonStatus("Speech");
        rightSide.setBalloonContent(translations[1]);
        panel.setRightSide(rightSide);

        scene.addPanel(panel);
        return scene;
    }

    //Returns a comic with only one character speaking the source and target for the left text of a vignette
    public static Comic createLeftVignetteComic(Figure figure) throws IOException {
        VignetteSchema vignetteSchema = null;
        while(vignetteSchema == null || vignetteSchema.getLeftTexts().isEmpty()) {
            vignetteSchema = VignetteManager.getRandomVignetteSchema();
        }
        Vignette vignette = vignetteSchema.getRandomVignette();
        Comic comic = new Comic();
        comic.addFigure(figure);
        comic.addScene(createLeftVignetteScene(vignette, figure));
        return comic;
    }

    private static Scene createLeftVignetteScene(Vignette vignette, Figure figure) throws IOException {
        Scene scene = new Scene();
        String leftText = vignette.getLeftText();
        Translator.batchTranslateList(List.of(leftText));
        String[] translations = Dictionary.getSourceAndTargetTranslations(leftText);

        //Source Panel
        Panel sourcePanel = new Panel();
        sourcePanel.setSetting(vignette.getBackgrounds());
        PanelSide sourceMiddle = new PanelSide();
        PanelFigure panelFigure = new PanelFigure(figure);
        panelFigure.setPose(vignette.getLeftPose());
        sourceMiddle.setPanelFigure(panelFigure);
        sourceMiddle.setBalloonStatus("Speech");
        sourceMiddle.setBalloonContent(translations[0]);
        sourcePanel.setMiddleSide(sourceMiddle);

        //Target Panel
        Panel targetPanel = new Panel();
        targetPanel.setSetting(vignette.getBackgrounds());
        PanelSide targetMiddle = new PanelSide();
        targetMiddle.setPanelFigure(panelFigure);
        targetMiddle.setBalloonStatus("Speech");
        targetMiddle.setBalloonContent(translations[1]);
        targetPanel.setMiddleSide(targetMiddle);

        scene.addPanel(sourcePanel);
        scene.addPanel(targetPanel);
        return scene;
    }
}

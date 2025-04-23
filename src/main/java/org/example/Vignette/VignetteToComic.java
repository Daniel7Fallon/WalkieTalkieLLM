package org.example.Vignette;

import org.example.Comic.*;
import org.example.Translation.Dictionary;
import org.example.XML.PanelTemplate;

import java.io.IOException;
import java.util.List;

public class VignetteToComic {

    public static Comic createComicFromVignette (List<Figure> figures, List<VignetteSchema> vignetteSchemas) {
        Comic comic = new Comic();

        comic.addAllFigures(figures);
        createScenesFromSchemas(comic, vignetteSchemas);

        return comic;
    }

    private static void createScenesFromSchemas(Comic comic, List<VignetteSchema> vignetteSchemas) {
        vignetteSchemas.stream()
                .map(VignetteSchema::getRandVignette)
                .forEach(vignette -> createSceneFromVignette(comic, vignette));
    }

    private static void createSceneFromVignette(Comic comic, Vignette vignette) {
        Scene scene = new Scene();
        comic.addScene(scene);
        createPanel(comic, scene, vignette, PanelTemplate.INTRO);
        createPanel(comic, scene, vignette, PanelTemplate.LEFT_SPEAKS);
        createPanel(comic, scene, vignette, PanelTemplate.RIGHT_SPEAKS);
    }

    private static void createPanel(Comic comic, Scene scene, Vignette vignette, PanelTemplate template) {
        Panel panel = new Panel();
        List<Figure> figures = comic.getFigures();

        PanelSide left = new PanelSide();
        PanelSide right = new PanelSide();

        PanelFigure leftFigure = new PanelFigure(figures.get(0));
        PanelFigure rightFigure = new PanelFigure(figures.get(1));

        leftFigure.setPose(vignette.getLeftPose());
        rightFigure.setPose(vignette.getRightPose());

        left.setPanelFigure(leftFigure);
        right.setPanelFigure(rightFigure);

        panel.setSetting(vignette.getBackgrounds());

        String[] sourceAndTarget;

        if (vignette.getLeftText() != null) {
            try {
                sourceAndTarget = Dictionary.getSourceAndTargetTranslations(vignette.getLeftText());
                if (sourceAndTarget == null) {
                    throw new IllegalArgumentException("Translation for " + vignette.getLeftText() + " doesn't exist.");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Call to dictionary failed.", e);
            }

            String translatedText = sourceAndTarget[1];

            switch (template) {
                case INTRO -> {

                }
                case LEFT_SPEAKS -> {
                    left.setBalloonStatus("speaking");
                    left.setBalloonContent(vignette.getLeftText());
                }
                case RIGHT_SPEAKS -> {
                    right.setBalloonStatus("speaking");
                    right.setBalloonContent(translatedText);
                }
                case BOTH_SPEAK -> {
                    left.setBalloonStatus("speaking");
                    left.setBalloonContent(vignette.getLeftText());

                    right.setBalloonStatus("speaking");
                    right.setBalloonContent(translatedText);
                }
            }
        }

        panel.setLeftSide(left);
        panel.setRightSide(right);
        scene.addPanel(panel);
    }

}

package org.example.Assets;

import org.example.Comic.*;
import org.example.Dictionary;
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

        switch (template) {
            case INTRO -> {
                panel.setSetting(vignette.getBackgrounds());
                //panel.setBelow(vignette.getBelow());
            }
            case LEFT_SPEAKS -> {
                PanelSide left = new PanelSide();
                //Figure leftFigure = comic.getFigureByName(vignette.getLeftSpeaker());
                //left.setPanelFigure(leftFigure);
                left.setBallonStatus("speaking");
                left.setBalloonContent(vignette.getLeftText());
                panel.setLeftSide(left);
            }
            case RIGHT_SPEAKS -> {
                PanelSide right = new PanelSide();
                //Figure rightFigure = comic.getFigureByName(vignette.getRightSpeaker());
                //right.setPanelFigure(rightFigure);
                right.setBallonStatus("speaking");
                right.setBalloonContent(vignette.getCombinedText());
                panel.setRightSide(right);
            }
        }

    }

}

package org.example.XML;

import org.example.Assets.Vignette;
import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.Comic.*;
import org.example.Dictionary;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {
    private static final String OUTPUT_FILENAME = "lesson.xml";
    private static final String DEFAULT_BORDER = "white";
    private static final String DEFAULT_DURATION = "500";

    public static void createLesson(List<Figure> figures, List<VignetteSchema> vignetteSchemas) {
        Element root = new Element("comic");
        Document document = new Document(root);

        root.addContent(createFiguresElement(figures, vignetteSchemas.getFirst().getRandVignette()));
        root.addContent(createScenesElement(vignetteSchemas, figures));

        generateXML(document);
    }

    public static void generateXMLFromComic(Comic comic) {
        Element root = new Element("comic");
        Document document = new Document(root);

        if (!comic.getFigures().isEmpty()) {
            root.addContent(createFiguresElement(comic.getFigures()));
        }

        if (!comic.getScenes().isEmpty()) {
            root.addContent(createScenesElement(comic.getScenes()));
        }

        generateXML(document);
    }

    private static void generateXML(Document document) {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileWriter writer = new FileWriter(OUTPUT_FILENAME)) {
            xmlOutputter.output(document, writer);
            System.out.println("Successfully generated XML.");
        } catch (IOException e) {
            System.err.println("Failed to generate XML: " + e.getMessage());
        }
    }

    // Common element creation methods
    private static Element createFiguresElement(List<Figure> figures, Vignette vignette) {
        Element figuresElement = new Element("figures");
        figuresElement.addContent(createFigureElement(figures.get(0), vignette.getLeftPose(), "right"));
        figuresElement.addContent(createFigureElement(figures.get(1), vignette.getRightPose(), "left"));
        return figuresElement;
    }

    private static Element createScenesElement(List<VignetteSchema> vignetteSchemas, List<Figure> figures) {
        Element scenesElement = new Element("scenes");
        vignetteSchemas.stream()
                .map(VignetteSchema::getRandVignette)
                .map(vignette -> createSceneElement(vignette, figures))
                .forEach(scenesElement::addContent);
        return scenesElement;
    }

    private static Element createSceneElement(Vignette vignette, List<Figure> figures) {
        Element sceneElement = new Element("scene");
        sceneElement.addContent(createPanelFromTemplate(vignette, figures, PanelTemplate.INTRO));
        sceneElement.addContent(createPanelFromTemplate(vignette, figures, PanelTemplate.LEFT_SPEAKS));
        sceneElement.addContent(createPanelFromTemplate(vignette, figures, PanelTemplate.BOTH_SPEAK));
        return sceneElement;
    }

    private static Element createPanelFromTemplate(Vignette vignette, List<Figure> figures, PanelTemplate template) {
        Element panelElement = new Element("panel");

        try {
            String[] sourceAndTarget = vignette.getLeftText() != null ?
                    Dictionary.getSourceAndTargetTranslations(vignette.getLeftText()) : null;

            // Left side
            String leftText = template.isLeftSpeaks() ? getText(sourceAndTarget, 0) : null;
            panelElement.addContent(createPanelSide("left", figures.get(0), vignette.getLeftPose(), leftText, "right"));

            // Right side
            String rightText = template.isRightSpeaks() ? getText(sourceAndTarget, 1) : null;
            panelElement.addContent(createPanelSide("right", figures.get(1), vignette.getRightPose(), rightText, "left"));

            addPanelMetadata(panelElement, vignette.getBackgrounds());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return panelElement;
    }

    // XMLBlueprint specific methods
    private static Element createFiguresElement(List<Figure> figures) {
        Element figuresElement = new Element("figures");
        figures.forEach(fig -> figuresElement.addContent(createFigureElement(fig)));
        return figuresElement;
    }

    private static Element createScenesElement(List<Scene> scenes) {
        Element scenesElement = new Element("scenes");
        scenes.forEach(scene -> scenesElement.addContent(createSceneElement(scene)));
        return scenesElement;
    }

    private static Element createSceneElement(Scene scene) {
        Element sceneElement = new Element("scene");
        scene.getPanels().forEach(panel -> sceneElement.addContent(createPanelElement(panel)));
        return sceneElement;
    }

    private static Element createPanelElement(Panel panel) {
        Element panelElement = new Element("panel");

        // Add panel metadata
        addIfNotNull(panelElement, "below", panel.getBelow());
        addIfNotNull(panelElement, "border", panel.getBorder());
        addIfNotNull(panelElement, "setting", panel.getSetting());

        // Add panel sides
        addPanelSideIfExists(panelElement, panel.getLeftSide(), "left");
        addPanelSideIfExists(panelElement, panel.getMiddleSide(), "middle");
        addPanelSideIfExists(panelElement, panel.getRightSide(), "right");

        return panelElement;
    }

    // Helper methods
    private static Element createFigureElement(Figure figure, String pose, String facing) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "name", figure.getName());
        addIfNotNull(figureElement, "appearance", figure.getAppearance());
        addIfNotNull(figureElement, "skin", figure.getSkin());
        addIfNotNull(figureElement, "hair", figure.getHair());
        addIfNotNull(figureElement, "pose", pose);
        addIfNotNull(figureElement, "facing", facing);
        return figureElement;
    }

    private static Element createPanelSide(String side, Figure figure, String pose, String text, String facing) {
        Element sideElement = new Element(side);
        sideElement.addContent(createBasicFigureElement(figure.getName(), pose, facing));

        if (text != null) {
            sideElement.addContent(createSpeechBalloon(text));
        }

        return sideElement;
    }

    private static Element createBasicFigureElement(String id, String pose, String facing) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", id);
        addIfNotNull(figureElement, "pose", pose);
        addIfNotNull(figureElement, "facing", facing);
        return figureElement;
    }

    private static Element createSpeechBalloon(String text) {
        Element balloonElement = new Element("balloon");
        balloonElement.setAttribute("status", "speech");
        addIfNotNull(balloonElement, "content", text);
        return balloonElement;
    }

    private static void addPanelMetadata(Element panelElement, String setting) {
        addIfNotNull(panelElement, "setting", setting);
        panelElement.addContent(new Element("border").setText(DEFAULT_BORDER));
        panelElement.addContent(new Element("duration").setText(DEFAULT_DURATION));
    }

    private static Element createFigureElement(Figure figure) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", figure.getId());
        addIfNotNull(figureElement, "name", figure.getName());
        addIfNotNull(figureElement, "appearance", figure.getAppearance());
        addIfNotNull(figureElement, "skin", figure.getSkin());
        addIfNotNull(figureElement, "hair", figure.getHair());
        addIfNotNull(figureElement, "lips", figure.getLips());
        addIfNotNull(figureElement, "facing", figure.getFacing());
        return figureElement;
    }

    private static void addPanelSideIfExists(Element panelElement, PanelSide panelSide, String sideName) {
        if (panelSide != null) {
            panelElement.addContent(createPanelSideElement(panelSide, sideName));
        }
    }

    private static Element createPanelSideElement(PanelSide panelSide, String sideName) {
        Element sideElement = new Element(sideName);

        if (panelSide.getPanelFigure() != null) {
            sideElement.addContent(createPanelFigureElement(panelSide.getPanelFigure()));
        }

        if (panelSide.getBalloonStatus() != null) {
            sideElement.addContent(createBalloonElement(panelSide.getBalloonStatus(), panelSide.getBalloonContent()));
        }

        return sideElement;
    }

    private static Element createPanelFigureElement(PanelFigure panelFigure) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", panelFigure.getId());
        addIfNotNull(figureElement, "name", panelFigure.getName());
        addIfNotNull(figureElement, "appearance", panelFigure.getAppearance());
        addIfNotNull(figureElement, "pose", panelFigure.getPose());
        addIfNotNull(figureElement, "facing", panelFigure.getFacing());
        addIfNotNull(figureElement, "skin", panelFigure.getSkin());
        addIfNotNull(figureElement, "hair", panelFigure.getHair());
        addIfNotNull(figureElement, "lips", panelFigure.getLips());
        addIfNotNull(figureElement, "horizontal", panelFigure.getHorizontal());
        addIfNotNull(figureElement, "vertical", panelFigure.getVertical());
        return figureElement;
    }

    private static Element createBalloonElement(String status, String content) {
        Element balloonElement = new Element("balloon");
        if (status != null) {
            balloonElement.setAttribute("status", status);
        }
        addIfNotNull(balloonElement, "content", content);
        return balloonElement;
    }

    private static String getText(String[] sourceAndTarget, int index) {
        return sourceAndTarget != null && sourceAndTarget.length > index ? sourceAndTarget[index] : null;
    }

    private static void addIfNotNull(Element parent, String childName, String value) {
        if (value != null) {
            parent.addContent(new Element(childName).setText(value));
        }
    }
}
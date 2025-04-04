package org.example.XML;

import org.example.Assets.Vignette;
import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.Comic.Figure;
import org.example.Dictionary;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {
    private static final int VIGNETTE_SCHEMA_START = 0;
    private static final int VIGNETTE_SCHEMA_END = 20;
    private static final String OUTPUT_FILENAME = "lesson.xml";
    private static final String DEFAULT_BORDER = "white";
    private static final String DEFAULT_DURATION = "500";

    public static void createLesson(List<Figure> figures) {
        Element root = new Element("comic");
        Document document = new Document(root);
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(VIGNETTE_SCHEMA_START, VIGNETTE_SCHEMA_END);

        root.addContent(createFiguresElement(figures, vignetteSchemas.getFirst().getRandVignette()));
        root.addContent(createScenesElement(vignetteSchemas, figures));

        generateXML(document);
    }

    public static void generateLessonFromBlueprint(XMLParser xmlBlueprint) {
        Element root = new Element("comic");
        Document document = new Document(root);

        if (!xmlBlueprint.getFigureDefinitions().isEmpty()) {
            root.addContent(createFiguresElement(xmlBlueprint.getFigureDefinitions()));
        }

        if (!xmlBlueprint.getScenes().isEmpty()) {
            root.addContent(createScenesElement(xmlBlueprint.getScenes()));
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
    private static Element createFiguresElement(List<XMLParser.FigureDefinition> figureDefinitions) {
        Element figuresElement = new Element("figures");
        figureDefinitions.forEach(figureDef -> figuresElement.addContent(createFigureElement(figureDef)));
        return figuresElement;
    }

    private static Element createScenesElement(List<XMLParser.Scene> scenes) {
        Element scenesElement = new Element("scenes");
        scenes.forEach(scene -> scenesElement.addContent(createSceneElement(scene)));
        return scenesElement;
    }

    private static Element createSceneElement(XMLParser.Scene scene) {
        Element sceneElement = new Element("scene");
        scene.getPanels().forEach(panel -> sceneElement.addContent(createPanelElement(panel)));
        return sceneElement;
    }

    private static Element createPanelElement(XMLParser.Panel panel) {
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

    private static Element createFigureElement(XMLParser.FigureDefinition figureDef) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", figureDef.getId());
        addIfNotNull(figureElement, "name", figureDef.getName());
        addIfNotNull(figureElement, "appearance", figureDef.getAppearance());
        addIfNotNull(figureElement, "skin", figureDef.getSkin());
        addIfNotNull(figureElement, "hair", figureDef.getHair());
        addIfNotNull(figureElement, "lips", figureDef.getLips());
        addIfNotNull(figureElement, "facing", figureDef.getFacing());
        return figureElement;
    }

    private static void addPanelSideIfExists(Element panelElement, XMLParser.PanelSide panelSide, String sideName) {
        if (panelSide != null) {
            panelElement.addContent(createPanelSideElement(panelSide, sideName));
        }
    }

    private static Element createPanelSideElement(XMLParser.PanelSide panelSide, String sideName) {
        Element sideElement = new Element(sideName);

        if (panelSide.getFigure() != null) {
            sideElement.addContent(createPanelFigureElement(panelSide.getFigure()));
        }

        if (panelSide.getBalloon() != null) {
            sideElement.addContent(createBalloonElement(panelSide.getBalloon()));
        }

        return sideElement;
    }

    private static Element createPanelFigureElement(XMLParser.PanelFigure panelFigure) {
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

    private static Element createBalloonElement(XMLParser.Balloon balloon) {
        Element balloonElement = new Element("balloon");
        if (balloon.getStatus() != null) {
            balloonElement.setAttribute("status", balloon.getStatus());
        }
        addIfNotNull(balloonElement, "content", balloon.getContent());
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
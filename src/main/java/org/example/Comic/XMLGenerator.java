package org.example.Comic;

import org.example.Assets.Vignette;
import org.example.Assets.VignetteManager;

import org.example.Assets.VignetteSchema;
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

    public static void createLesson(List<Figure> figures) {
        Element root = new Element("comic");
        Document document = new Document(root);
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(VIGNETTE_SCHEMA_START, VIGNETTE_SCHEMA_END);

        root.addContent(createFiguresElement(figures, vignetteSchemas.getFirst().getRandVignette()));
        root.addContent(createScenesElement(vignetteSchemas, figures));

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

    private static Element createFiguresElement(List<Figure> figures, Vignette vignette) {
        Element figuresElement = new Element("figures");
        Figure leftFigure = figures.get(0);
        Figure rightFigure = figures.get(1);

        figuresElement.addContent(createFigureElement(leftFigure, vignette.getLeftPose(), "right"));
        figuresElement.addContent(createFigureElement(rightFigure, vignette.getRightPose(), "left"));

        return figuresElement;
    }

    private static Element createFigureElement(Figure figure, String pose, String facing) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "name", figure.getName());
        addIfNotNull(figureElement, "appearance", figure.getAppearance());
        addIfNotNull(figureElement, "skin", figure.getSkin());
        addIfNotNull(figureElement, "hair", figure.getHair());
        addIfNotNull(figureElement, "pose", pose);
        figureElement.addContent(new Element("facing").setText(facing));
        return figureElement;
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

    // Can create templates later for different types of scenes
    private static Element createPanelFromTemplate(Vignette vignette, List<Figure> figures, PanelTemplate template) {
        Element panelElement = new Element("panel");

        try {
            String[] sourceAndTarget = null;
            if (vignette.getLeftText() != null) {
                sourceAndTarget = Dictionary.getSourceAndTargetTranslations(vignette.getLeftText());
            }

            // Left side
            String leftText = template.isLeftSpeaks() ? (sourceAndTarget != null ? sourceAndTarget[1] : null) : null;
            panelElement.addContent(createPanelSide("left", figures.get(0), vignette.getLeftPose(), leftText, "right"));

            // Right side
            String rightText = template.isRightSpeaks() ? (sourceAndTarget != null ? sourceAndTarget[0] : null) : null;
            panelElement.addContent(createPanelSide("right", figures.get(1), vignette.getRightPose(), rightText, "left"));

            addIfNotNull(panelElement, "setting", vignette.getBackgrounds());
            panelElement.addContent(new Element("border").setText("white"));
            panelElement.addContent(new Element("duration").setText("500")); // Make this not static later

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return panelElement;
    }

    private static Element createPanelSide(String side, Figure figure, String pose, String text, String facing) {
        Element sideElement = new Element(side);

        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", figure.getName());
        addIfNotNull(figureElement, "pose", pose);
        addIfNotNull(figureElement, "facing", facing);
        sideElement.addContent(figureElement);

        if (text != null) {
            Element balloonElement = new Element("balloon");
            balloonElement.setAttribute("status", "speech");
            addIfNotNull(balloonElement, "content", text);
            sideElement.addContent(balloonElement);
        }

        return sideElement;
    }

    private static void addIfNotNull(Element parent, String childName, String value) {
        if (value != null) {
            parent.addContent(new Element(childName).setText(value));
        }
    }
}

/*
            Structure to generate:

            <panel>
                <above><\above>
                <left>
                    <figure>
                        <id></id>
                        <pose></pose>
                        <facing></facing>
                    </figure>
                    <balloon>
                        <content></content>
                    </balloon>
                </left>
                <right>
                    <figure>
                        <id></id>
                        <pose></pose>
                        <facing></facing>
                    </figure>
                    <balloon>
                        <content></content>
                    </balloon>
                </right>
                <setting></setting>
                <border></border>
                <duration></duration>
                <print></print>
                <audio></audio>
            </panel>

         */
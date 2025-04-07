package org.example.XML;

import org.example.Comic.*;
import org.example.Translator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class XMLGenerator {
    private static final String DEFAULT_BORDER = "white";
    //private static final String DEFAULT_DURATION = "500";

    public static void generateBilingualXML(String xmlPath, String target) {
        try {
            String xmlContent = new String(Files.readAllBytes(Paths.get(xmlPath)));

            Comic conjugationTemplate = XMLParser.parseComic(xmlContent);

            List<String> balloonContents = conjugationTemplate.getAllBalloonContent();
            Translator.batchTranslateList(balloonContents);

            ComicPostProcessor.addTranslationPanels(conjugationTemplate);
            generateXMLFromComic(conjugationTemplate, target);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void generateXMLFromComic(Comic comic, String filename) {
        Element root = new Element("comic");
        Document document = new Document(root);

        // Add figures
        List<Figure> figures = comic.getFigures();
        if (figures != null && !figures.isEmpty()) {
            Element figuresElement = new Element("figures");
            for (Figure figure : figures) {
                figuresElement.addContent(createFigureElement(figure));
            }
            root.addContent(figuresElement);
        }

        // Add scenes
        List<Scene> scenes = comic.getScenes();
        if (scenes != null && !scenes.isEmpty()) {
            Element scenesElement = new Element("scenes");
            for (Scene scene : scenes) {
                scenesElement.addContent(createSceneElement(scene));
            }
            root.addContent(scenesElement);
        }

        // Write XML to file
        writeXML(document, filename);
    }

    private static Element createFigureElement(Figure figure) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", figure.getId());
        addIfNotNull(figureElement, "name", figure.getName());
        addIfNotNull(figureElement, "appearance", figure.getAppearance());
        addIfNotNull(figureElement, "skin", figure.getSkin());
        addIfNotNull(figureElement, "hair", figure.getHair());
        addIfNotNull(figureElement, "beard", figure.getBeard());
        addIfNotNull(figureElement, "hairLength", figure.getHairLength());
        addIfNotNull(figureElement, "hairStyle", figure.getHairStyle());
        addIfNotNull(figureElement, "lips", figure.getLips());
        addIfNotNull(figureElement, "facing", figure.getFacing());
        return figureElement;
    }

    private static Element createSceneElement(Scene scene) {
        Element sceneElement = new Element("scene");
        for (Panel panel : scene.getPanels()) {
            sceneElement.addContent(createPanelElement(panel));
        }
        return sceneElement;
    }

    private static Element createPanelElement(Panel panel) {
        Element panelElement = new Element("panel");
        addIfNotNull(panelElement, "setting", panel.getSetting());
        addIfNotNull(panelElement, "below", panel.getBelow());
        addIfNotNull(panelElement, "border", panel.getBorder() != null ? panel.getBorder() : DEFAULT_BORDER);
        //panelElement.addContent(new Element("duration").setText(DEFAULT_DURATION));

        addPanelSideIfExists(panelElement, panel.getLeftSide(), "left");
        addPanelSideIfExists(panelElement, panel.getMiddleSide(), "middle");
        addPanelSideIfExists(panelElement, panel.getRightSide(), "right");

        return panelElement;
    }

    private static void addPanelSideIfExists(Element panelElement, PanelSide side, String sideName) {
        if (side != null) {
            Element sideElement = new Element(sideName);

            if (side.getPanelFigure() != null) {
                sideElement.addContent(createPanelFigureElement(side.getPanelFigure()));
            }

            if (side.getBalloonStatus() != null) {
                Element balloonElement = new Element("balloon");
                balloonElement.setAttribute("status", side.getBalloonStatus());
                addIfNotNull(balloonElement, "content", side.getBalloonContent());
                sideElement.addContent(balloonElement);
            }

            panelElement.addContent(sideElement);
        }
    }

    private static Element createPanelFigureElement(PanelFigure pf) {
        Element figureElement = new Element("figure");
        addIfNotNull(figureElement, "id", pf.getId());
        addIfNotNull(figureElement, "name", pf.getName());
        addIfNotNull(figureElement, "appearance", pf.getAppearance());
        addIfNotNull(figureElement, "pose", pf.getPose());
        addIfNotNull(figureElement, "facing", pf.getFacing());
        addIfNotNull(figureElement, "skin", pf.getSkin());
        addIfNotNull(figureElement, "hair", pf.getHair());
        addIfNotNull(figureElement, "beard", pf.getBeard());
        addIfNotNull(figureElement, "hairLength", pf.getHairLength());
        addIfNotNull(figureElement, "hairStyle", pf.getHairStyle());
        addIfNotNull(figureElement, "lips", pf.getLips());
        addIfNotNull(figureElement, "horizontal", pf.getHorizontal());
        addIfNotNull(figureElement, "vertical", pf.getVertical());
        return figureElement;
    }

    private static void writeXML(Document document, String filename) {
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileWriter writer = new FileWriter(filename)) {
            outputter.output(document, writer);
            System.out.println("Successfully generated XML: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing XML: " + e.getMessage());
        }
    }

    private static void addIfNotNull(Element parent, String name, String value) {
        if (value != null) {
            parent.addContent(new Element(name).setText(value));
        }
    }
}
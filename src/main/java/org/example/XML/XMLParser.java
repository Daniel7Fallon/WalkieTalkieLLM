package org.example.XML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.example.Comic.*;

public class XMLParser {

    public static Comic parseComicFromResourcesPath(String xmlPath) throws JDOMException, IOException {
        ClassLoader classLoader = XMLParser.class.getClassLoader();
        try(InputStream inputStream = classLoader.getResourceAsStream(xmlPath)) {
            if(inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + xmlPath);
            }
            String xmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return parseComic(xmlContent);
        }
    }

    public static Comic parseComicFromFilePath(String xmlPath) throws IOException, JDOMException {
        String xmlContent = new String(Files.readAllBytes(Paths.get(xmlPath)));
        return parseComic(xmlContent);
    }

    public static Comic parseComic(String xmlContent) throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlContent));
        Element rootElement = document.getRootElement();

        Comic comic = new Comic();
        parseFigures(rootElement.getChild("figures"), comic);
        parseScenes(rootElement.getChild("scenes"), comic);

        return comic;
    }

    private static void parseFigures(Element figuresElement, Comic comic) {
        List<Figure> figures = new ArrayList<>();
        if (figuresElement == null) return;
        for (Element figureElement : figuresElement.getChildren("figure")) {
            Figure figure = new Figure();
            figure.setId(figureElement.getChildText("id"));
            figure.setName(figureElement.getChildText("name"));
            figure.setAppearance(figureElement.getChildText("appearance"));
            figure.setSkin(figureElement.getChildText("skin"));
            figure.setHair(figureElement.getChildText("hair"));
            figure.setLips(figureElement.getChildText("lips"));
            figure.setFacing(figureElement.getChildText("facing"));
            figures.add(figure);
        }
        comic.addAllFigures(figures);
    }

    private static void parseScenes(Element scenesElement, Comic comic) {
        if (scenesElement == null) return;
        List<Scene> scenes = new ArrayList<>();
        for (Element sceneElement : scenesElement.getChildren("scene")) {
            Scene scene = new Scene();
            for (Element panelElement : sceneElement.getChildren("panel")) {
                Panel panel = new Panel();
                // Parse panel elements
                panel.setAbove(panelElement.getChildText("above"));
                panel.setBelow(panelElement.getChildText("below"));
                panel.setBorder(panelElement.getChildText("border"));
                panel.setSetting(panelElement.getChildText("setting"));
                // Parse panel sides (left, middle, right)
                parsePanelSide(panel, panelElement.getChild("left"), "left", comic);
                parsePanelSide(panel, panelElement.getChild("middle"), "middle", comic);
                parsePanelSide(panel, panelElement.getChild("right"), "right", comic);
                scene.addPanel(panel);
            }
            scenes.add(scene);
        }
        comic.addAllScenes(scenes);
    }

    private static void parsePanelSide(Panel panel, Element sideElement, String side, Comic comic) {
        if (sideElement == null) return;

        PanelSide panelSide = new PanelSide();

        // Parse figure in this side
        Element figureElement = sideElement.getChild("figure");
        if (figureElement != null) {
            String id = figureElement.getChildText("id");
            Figure figure = comic.getFigureById(id);
            if (figure == null) figure = new Figure();
            PanelFigure panelFigure = new PanelFigure(figure);

            panelFigure.setName(figureElement.getChildText("name"));
            panelFigure.setAppearance(figureElement.getChildText("appearance"));
            panelFigure.setPose(figureElement.getChildText("pose"));
            panelFigure.setFacing(figureElement.getChildText("facing"));
            panelFigure.setSkin(figureElement.getChildText("skin"));
            panelFigure.setHair(figureElement.getChildText("hair"));
            panelFigure.setLips(figureElement.getChildText("lips"));
            panelFigure.setHorizontal(figureElement.getChildText("horizontal"));
            panelFigure.setVertical(figureElement.getChildText("vertical"));

            panelSide.setPanelFigure(panelFigure);
        }

        // Parse balloon if exists
        Element balloonElement = sideElement.getChild("balloon");
        if (balloonElement != null) {
            panelSide.setBalloonStatus(balloonElement.getAttributeValue("status"));
            panelSide.setBalloonContent(balloonElement.getChildText("content"));
        }

        // Add to appropriate side of panel
        switch (side) {
            case "left":
                panel.setLeftSide(panelSide);
                break;
            case "middle":
                panel.setMiddleSide(panelSide);
                break;
            case "right":
                panel.setRightSide(panelSide);
                break;
        }
    }
}

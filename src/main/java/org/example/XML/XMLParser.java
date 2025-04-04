package org.example.XML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private List<FigureDefinition> figureDefinitions;
    private List<Scene> scenes;

    public XMLParser() {
        this.figureDefinitions = new ArrayList<>();
        this.scenes = new ArrayList<>();
    }

    public void loadFromXML(String xmlContent) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new StringReader(xmlContent));
        Element rootElement = document.getRootElement();

        parseFigureDefinitions(rootElement.getChild("figures"));
        parseScenes(rootElement.getChild("scenes"));
    }

    private void parseFigureDefinitions(Element figuresElement) {
        if (figuresElement == null) return;

        for (Element figureElement : figuresElement.getChildren("figure")) {
            FigureDefinition figure = new FigureDefinition();
            figure.setId(figureElement.getChildText("id"));
            figure.setName(figureElement.getChildText("name"));
            figure.setAppearance(figureElement.getChildText("appearance"));
            figure.setSkin(figureElement.getChildText("skin"));
            figure.setHair(figureElement.getChildText("hair"));
            figure.setLips(figureElement.getChildText("lips"));
            figure.setFacing(figureElement.getChildText("facing"));

            figureDefinitions.add(figure);
        }
    }

    private void parseScenes(Element scenesElement) {
        if (scenesElement == null) return;

        for (Element sceneElement : scenesElement.getChildren("scene")) {
            Scene scene = new Scene();

            for (Element panelElement : sceneElement.getChildren("panel")) {
                Panel panel = new Panel();

                // Parse panel elements
                panel.setBelow(panelElement.getChildText("below"));
                panel.setBorder(panelElement.getChildText("border"));
                panel.setSetting(panelElement.getChildText("setting"));

                // Parse panel sides (left, middle, right)
                parsePanelSide(panel, panelElement.getChild("left"), "left");
                parsePanelSide(panel, panelElement.getChild("middle"), "middle");
                parsePanelSide(panel, panelElement.getChild("right"), "right");

                scene.addPanel(panel);
            }

            scenes.add(scene);
        }
    }

    private void parsePanelSide(Panel panel, Element sideElement, String side) {
        if (sideElement == null) return;

        PanelSide panelSide = new PanelSide();

        // Parse figure in this side
        Element figureElement = sideElement.getChild("figure");
        if (figureElement != null) {
            PanelFigure figure = new PanelFigure();
            figure.setId(figureElement.getChildText("id"));
            figure.setName(figureElement.getChildText("name"));
            figure.setAppearance(figureElement.getChildText("appearance"));
            figure.setPose(figureElement.getChildText("pose"));
            figure.setFacing(figureElement.getChildText("facing"));
            figure.setSkin(figureElement.getChildText("skin"));
            figure.setHair(figureElement.getChildText("hair"));
            figure.setLips(figureElement.getChildText("lips"));
            figure.setHorizontal(figureElement.getChildText("horizontal"));
            figure.setVertical(figureElement.getChildText("vertical"));

            panelSide.setFigure(figure);
        }

        // Parse balloon if exists
        Element balloonElement = sideElement.getChild("balloon");
        if (balloonElement != null) {
            Balloon balloon = new Balloon();
            balloon.setStatus(balloonElement.getAttributeValue("status"));
            balloon.setContent(balloonElement.getChildText("content"));

            panelSide.setBalloon(balloon);
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

    // Getters
    public List<FigureDefinition> getFigureDefinitions() {
        return figureDefinitions;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    // Inner classes representing the comic structure
    public static class FigureDefinition {
        private String id;
        private String name;
        private String appearance;
        private String skin;
        private String hair;
        private String lips;
        private String facing;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAppearance() { return appearance; }
        public void setAppearance(String appearance) { this.appearance = appearance; }
        public String getSkin() { return skin; }
        public void setSkin(String skin) { this.skin = skin; }
        public String getHair() { return hair; }
        public void setHair(String hair) { this.hair = hair; }
        public String getLips() { return lips; }
        public void setLips(String lips) { this.lips = lips; }
        public String getFacing() { return facing; }
        public void setFacing(String facing) { this.facing = facing; }
    }

    public static class Scene {
        private List<Panel> panels = new ArrayList<>();

        public void addPanel(Panel panel) {
            panels.add(panel);
        }

        public List<Panel> getPanels() {
            return panels;
        }
    }

    public static class Panel {
        private String below;
        private String border;
        private String setting;
        private PanelSide leftSide;
        private PanelSide middleSide;
        private PanelSide rightSide;

        // Getters and setters
        public String getBelow() { return below; }
        public void setBelow(String below) { this.below = below; }
        public String getBorder() { return border; }
        public void setBorder(String border) { this.border = border; }
        public String getSetting() { return setting; }
        public void setSetting(String setting) { this.setting = setting; }
        public PanelSide getLeftSide() { return leftSide; }
        public void setLeftSide(PanelSide leftSide) { this.leftSide = leftSide; }
        public PanelSide getMiddleSide() { return middleSide; }
        public void setMiddleSide(PanelSide middleSide) { this.middleSide = middleSide; }
        public PanelSide getRightSide() { return rightSide; }
        public void setRightSide(PanelSide rightSide) { this.rightSide = rightSide; }
    }

    public static class PanelSide {
        private PanelFigure figure;
        private Balloon balloon;

        public String getBalloonContent() {
            if (balloon == null) {
                return null;
            }
            return this.getBalloon().getContent();
        }

        // Getters and setters
        public PanelFigure getFigure() { return figure; }
        public void setFigure(PanelFigure figure) { this.figure = figure; }
        public Balloon getBalloon() { return balloon; }
        public void setBalloon(Balloon balloon) { this.balloon = balloon; }
    }

    public static class PanelFigure {
        private String id;
        private String name;
        private String appearance;
        private String pose;
        private String facing;
        private String skin;
        private String hair;
        private String lips;
        private String horizontal;
        private String vertical;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAppearance() { return appearance; }
        public void setAppearance(String appearance) { this.appearance = appearance; }
        public String getPose() { return pose; }
        public void setPose(String pose) { this.pose = pose; }
        public String getFacing() { return facing; }
        public void setFacing(String facing) { this.facing = facing; }
        public String getSkin() { return skin; }
        public void setSkin(String skin) { this.skin = skin; }
        public String getHair() { return hair; }
        public void setHair(String hair) { this.hair = hair; }
        public String getLips() { return lips; }
        public void setLips(String lips) { this.lips = lips; }
        public String getHorizontal() { return horizontal; }
        public void setHorizontal(String horizontal) { this.horizontal = horizontal; }
        public String getVertical() { return vertical; }
        public void setVertical(String vertical) { this.vertical = vertical; }
    }

    public static class Balloon {
        private String status;
        private String content;

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}

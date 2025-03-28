package org.example.Comic;

import org.example.Assets.Vignette;
import org.example.Assets.VignetteManager;

import org.example.Assets.VignetteSchema;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLGenerator {

    public static void generateXML(Document document) {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat()); // Make the XML look nice and human-readable
        try (FileWriter writer = new FileWriter("lesson.xml")) {
            xmlOutputter.output(document, writer);
            System.out.println("Successfully generated xml.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document createDocument() {
        Element root = new Element("comic");
        Document document = new Document(root);
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemas();

        root.addContent(getFigures());
        root.addContent(getScenes(vignetteSchemas));

        return document;
    }

    private static Element getFigures() {
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(0, 20);
        Vignette vignette = vignetteSchemas.getFirst().getRandVignette();

        Element figuresElement = new Element("figures");
        Figure rightFigure = new Figure();
        Figure leftFigure = new Figure();

        Element leftFigureElement = new Element("figure");
        addIfNotNull(leftFigureElement, "name", leftFigure.getName());
        addIfNotNull(leftFigureElement, "appearance", leftFigure.getAppearance());
        addIfNotNull(leftFigureElement, "skin", leftFigure.getSkin());
        addIfNotNull(leftFigureElement, "hair", leftFigure.getHair());
        addIfNotNull(leftFigureElement, "pose", vignette.getLeftPose());
        leftFigureElement.addContent(new Element("facing").setText("right"));

        Element rightFigureElement = new Element("figure");
        addIfNotNull(rightFigureElement, "name", rightFigure.getName());
        addIfNotNull(rightFigureElement, "appearance", rightFigure.getAppearance());
        addIfNotNull(rightFigureElement, "skin", rightFigure.getSkin());
        addIfNotNull(rightFigureElement, "hair", rightFigure.getHair());
        addIfNotNull(rightFigureElement, "pose", vignette.getRightPose());
        rightFigureElement.addContent(new Element("facing").setText("right"));
        figuresElement.addContent(rightFigureElement);

        return figuresElement;
    }

    private static Element getScenes(List<VignetteSchema> vignetteSchemas) {
        Element scenes = new Element("scenes");

        for (VignetteSchema vignetteSchema : vignetteSchemas) {
            scenes.addContent(getScene(vignetteSchema.getRandVignette()));
        }

        return scenes;
    }

    // Scene can contain a scene and/or a rubric
    private static Element getScene(Vignette vignette) {
        Element scene = new Element("scene");

        //generatePanel();

        return scene;
    }

    private Element generatePanel () {
        Element panel = new Element("panel");

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

        return panel;
    }

    // Helper method to add element only if value is not null
    private static void addIfNotNull(Element parent, String childName, String value) {
        if (value != null) {
            parent.addContent(new Element(childName).setText(value));
        }
    }

}

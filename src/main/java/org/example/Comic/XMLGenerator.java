package org.example.Comic;

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


        // TODO


        return document;
    }

    private static Element getFigures() {
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(0, 20);

        Element figuresElement = new Element("figures");
        Figure rightFigure = new Figure();
        Figure leftFigure = new Figure();

        Element leftFigureElement = new Element("figure");
        leftFigureElement.addContent(new Element("name").setText(leftFigure.getName()));
        leftFigureElement.addContent(new Element("appearance").setText(leftFigure.getAppearance()));
        leftFigureElement.addContent(new Element("skin").setText(leftFigure.getSkin()));
        leftFigureElement.addContent(new Element("hair").setText(leftFigure.getHair()));
        leftFigureElement.addContent(new Element("pose").setText(vignetteSchemas.getFirst().getLeftPose()));
        leftFigureElement.addContent(new Element("facing").setText("right"));

        Element rightFigureElement = new Element("figure");
        rightFigureElement.addContent(new Element("name").setText(rightFigure.getName()));
        rightFigureElement.addContent(new Element("appearance").setText(rightFigure.getAppearance()));
        rightFigureElement.addContent(new Element("skin").setText(rightFigure.getSkin()));
        rightFigureElement.addContent(new Element("hair").setText(rightFigure.getHair()));
        rightFigureElement.addContent(new Element("pose").setText(vignetteSchemas.getFirst().getRightPoses().getFirst()));
        rightFigureElement.addContent(new Element("facing").setText("right"));
        figuresElement.addContent(rightFigureElement);

        return figuresElement;
    }

    private static Element getScenes(List<VignetteSchema> vignetteSchemas) {
        Element scenes = new Element("scenes");

        for (VignetteSchema vignetteSchema : vignetteSchemas) {
            scenes.addContent(getScene(vignetteSchema));
        }

        return scenes;
    }

    // Scene can contain a scene and/or a rubric
    private static Element getScene(VignetteSchema vignetteSchema) {
        Element scene = new Element("scene");

        /*
            Rubric Structure:

            <rubric>
                <image><\image>
                <above><\above>
                <below><\below>
                <duration><\duration>
            <\rubric>
         */

        // TODO

        return scene;
    }

    private Element generatePanel (VignetteSchema vignetteSchema) {
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



}

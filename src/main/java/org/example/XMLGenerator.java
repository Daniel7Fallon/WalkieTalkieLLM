package org.example;

import org.example.Assets.VignetteManager;

import org.example.Assets.VignetteSchema;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XMLGenerator {

    private void generateXML(Document document) {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat()); // Make the XML look nice and human-readable
        try (FileWriter writer = new FileWriter("lesson.xml")) {
            xmlOutputter.output(document, writer);
            System.out.println("Successfully generated xml.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(VignetteManager vignetteManager) {
        Element root = new Element("comic");
        Document document = new Document(root);
        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemas();

        root.addContent(getFigures(vignetteManager));
        root.addContent(getScenes(vignetteSchemas));


        // TODO


        return document;
    }

    private Element getFigures(VignetteManager vignetteManager) {
        Element figuresElement = new Element("figures");

        /*
        // Assuming VignetteManager has a method to get all figures
        List<VignetteSchema.Figure> figures = vignetteManager.getFigures();

        for (VignetteSchema.Figure figure : figures) {
            Element figureElement = new Element("figure");

            figureElement.addContent(new Element("name").setText(figure.getName()));
            figureElement.addContent(new Element("appearance").setText(figure.getAppearance()));
            figureElement.addContent(new Element("skin").setText(figure.getSkin()));
            figureElement.addContent(new Element("hair").setText(figure.getHair()));
            figureElement.addContent(new Element("pose").setText(figure.getPose()));
            figureElement.addContent(new Element("facing").setText(figure.getFacing()));

            figuresElement.addContent(figureElement);
        }
        */

        return figuresElement;
    }

    private Element getScenes(List<VignetteSchema> vignetteSchemas) {
        Element scenes = new Element("scenes");

        for (VignetteSchema vignetteSchema : vignetteSchemas) {
            scenes.addContent(getScene(vignetteSchema));
        }

        return scenes;
    }

    // Scene can contain a scene and/or a rubric
    private Element getScene(VignetteSchema vignetteSchema) {
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

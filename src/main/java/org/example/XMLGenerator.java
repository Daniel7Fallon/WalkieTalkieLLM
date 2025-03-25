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
            System.out.println("Successfully generated lesson");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(VignetteManager vignetteManager) {
        Element root = new Element("comic");
        Document document = new Document(root);

        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemas();
        Element figures = getFigures();



        return document;
    }

    private Element getFigures() {

        /*
            TODO:
            Need this code to get the information from either the vignette manager or from a new figure class
            and input the data for two figures into the element in the correct format.
         */

        return new Element("figures");
    }

    private Element getScenes(List<VignetteSchema> vignetteSchemas) {
        Element scenes = new Element("scenes");

        // TODO

        return scenes;
    }

    private Element getScene(VignetteSchema vignetteSchema) {
        Element scene = new Element("scene");

        // TODO

        return scene;
    }



}

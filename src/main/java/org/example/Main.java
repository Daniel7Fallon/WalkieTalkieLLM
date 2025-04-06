package org.example;

import org.example.Comic.Comic;
import org.example.Comic.ComicPostProcessor;
import org.example.Comic.PanelSide;
import org.example.XML.VignetteToComic;
import org.jdom2.JDOMException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.Comic.Figure;
import org.example.XML.XMLParser;
import org.example.XML.XMLGenerator;

import static org.example.Utils.StringUtil.removePluralIdentifier;


public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java -jar <pathToJar> <pathToConfigFile>");
            return;
        }
        String configFilePath = args[0];
        try {
            ConfigurationFile.initialize(configFilePath);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid config file: " + e.getMessage());
            return;
        }
        VignetteManager.initialize();
        Dictionary.initialize();

        try {
            String xmlPath = ConfigurationFile.getValue("SPECIFICATION_XML");
            String xmlContent = new String(Files.readAllBytes(Paths.get(xmlPath)));

            Comic conjugationTemplate = XMLParser.parseComic(xmlContent);

            List<String> balloonContents = conjugationTemplate.getAllBalloonContent();
            Translator.batchTranslateList(balloonContents);

            ComicPostProcessor.addTranslationPanels(conjugationTemplate);
            XMLGenerator.generateXMLFromComic(conjugationTemplate, ConfigurationFile.getValue("LESSON_TARGET"));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            System.out.println("\nTranslating first 5 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,5);

            System.out.println("\nTranslating first 10 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,10);

            System.out.println("\nTranslating first 20 of vignette schemas");
            Translator.translateVignetteSchemasInRange(0,20);

        } catch (Exception e) {
            e.printStackTrace();
        }

        VignetteSchema firstVS = VignetteManager.getVignetteSchemas().getFirst();
        System.out.println("First VignetteSchema: " + firstVS);
        for(int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". " + firstVS.getRandVignette());
        }

        ArrayList<Figure> figures = new ArrayList<Figure>();
        Figure leftFigure = new Figure();
        leftFigure.setName("Daniel");
        leftFigure.setSkin("Brown");

        Figure rightFigure = new Figure();
        rightFigure.setName("Harry");
        rightFigure.setSkin("White");

        figures.add(leftFigure);
        figures.add(rightFigure);

        List<VignetteSchema> vignetteSchemas = VignetteManager.getVignetteSchemasInRange(0, 10);
        Comic comic = VignetteToComic.createComicFromVignette(figures, vignetteSchemas);
        XMLGenerator.generateXMLFromComic(comic, "lesson.xml");
    }


}
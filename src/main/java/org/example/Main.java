package org.example;

import org.example.Comic.Comic;
import org.example.Comic.PanelSide;
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
            System.out.println("\n=== Loading Comic Specifications ===");
            String xmlPath = ConfigurationFile.getValue("SPECIFICATION_XML");
            String xmlContent = new String(Files.readAllBytes(Paths.get(xmlPath)));

            Comic comic = XMLParser.parseComic(xmlContent);

            System.out.println("Successfully loaded specifications!");
            System.out.println("Total Figures Defined: " + comic.getFigures().size());
            System.out.println("Total Scenes Found: " + comic.getScenes().size());

            //Proof that the program is working as intended

            // Print figures
            System.out.println("\n=== Character Roster ===");
            comic.getFigures().stream()
                    .forEach(fig -> System.out.println(
                            " - " + fig.getName() +
                            " (" + fig.getId() + ")" +
                            " | Appearance: " + fig.getAppearance()
                    ));

            System.out.println("\n=== Scene Structure ===");
            comic.getScenes().forEach(scene -> {
                System.out.println("\nScene with " + scene.getPanels().size() + " panels:");
                scene.getPanels().forEach(panel -> {
                    System.out.println("  Panel Setting: " + panel.getSetting());
                    if(panel.getLeftSide() != null) {
                        System.out.println("  Left Side: " +
                                (panel.getLeftSide().getPanelFigure() != null ?
                                        panel.getLeftSide().getPanelFigure().getName() : "No figure") +
                                " | Dialogue: " + panel.getLeftSide().getBalloonContent());
                    }
                    // Would add other blocks for middle/right here if we needed to, this code is just proof that the xml is parsed in memory
                });
            });

            List<String> balloonContents = comic.getAllBalloonContent();
            System.out.println(balloonContents);
            Translator.batchTranslateList(balloonContents);
            for(String input : balloonContents) {
                System.out.println("Source: " + removePluralIdentifier(input) + "\t | Target: " + Dictionary.getSourceAndTargetTranslations(input)[1]);
            }

        } catch (IOException | JDOMException e) {
            System.err.println("Failed to process specifications XML: " + e.getMessage());
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
        XMLGenerator.createLesson(figures, vignetteSchemas);
    }


}
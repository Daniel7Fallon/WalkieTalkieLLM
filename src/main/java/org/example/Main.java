package org.example;

import org.jdom2.JDOMException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.example.Assets.VignetteManager;
import org.example.Assets.VignetteSchema;
import org.example.Comic.Figure;
import org.example.Comic.XMLBlueprint;
import org.example.Comic.XMLGenerator;


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

            XMLBlueprint comic = new XMLBlueprint();
            comic.loadFromXML(xmlContent);

            System.out.println("Successfully loaded specifications!");
            System.out.println("Total Figures Defined: " + comic.getFigureDefinitions().size());
            System.out.println("Total Scenes Found: " + comic.getScenes().size());

            //Proof that the program is working as intended

            // Print figures
            System.out.println("\n=== Character Roster ===");
            comic.getFigureDefinitions().stream()
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
                                (panel.getLeftSide().getFigure() != null ?
                                        panel.getLeftSide().getFigure().getId() : "No figure") +
                                " | Dialogue: " + getBalloonText(panel.getLeftSide()));
                    }
                    // Would add other blocks for middle/right here if we needed to, this code is just proof that the xml is parsed in memory
                });
            });

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

        XMLGenerator.createLesson(figures);
    }

    private static String getBalloonText(XMLBlueprint.PanelSide side) {
        if(side.getBalloon() == null) return "None";
        return "\"" + side.getBalloon().getContent() + "\"";
    }
}
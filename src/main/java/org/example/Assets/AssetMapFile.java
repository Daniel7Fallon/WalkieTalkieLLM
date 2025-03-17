package org.example.Assets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetMapFile {
    private static final List<Vignette> vignettes = new ArrayList<>();

    //Deserialises pose pairings with backgrounds.tsv
    public static void initialize(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); //skip first line
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t", 5);
                String leftPose = tokens[0];
                List<String> combinedText = parseCell(tokens[1]);
                List<String> leftText = parseCell(tokens[2]);
                List<String> rightPose = parseCell(tokens[3]);
                List<String> backgrounds = parseCell(tokens[4]);
                vignettes.add(new Vignette(leftPose, combinedText, leftText, rightPose, backgrounds));
            }
            System.out.println("AssetMapping file loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading AssetMapping file: " + e.getMessage());
        }
    }

    //Parses the combinedText etc. fields on ", "
    private static List<String> parseCell(String cell) {
        String[] tokens = cell.split(", ");
        List<String> output = new ArrayList<>();
        for (String token : tokens) {
            String cleanToken = token.trim();
            if(cleanToken.isEmpty()) continue;
            output.add(cleanToken);
        }
        return output;
    }

    public static List<Vignette> getVignettes() {
        return vignettes;
    }

    public static void printVignettes() {
        for(Vignette vignette : vignettes) {
            System.out.println(vignette);
        }
    }

    public static List<Vignette> getVignettesInRange(int start, int end) {
        List<Vignette> result = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            result.add(vignettes.get(i));
        }
        return result;
    }
}

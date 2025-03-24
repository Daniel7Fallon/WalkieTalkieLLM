package org.example.Assets;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AssetMapFile {
    private static final List<VignetteSchema> vignetteSchemas = new ArrayList<>();

    //Deserialises pose pairings with backgrounds.tsv
    public static void initialize() {
        InputStream inputStream = AssetMapFile.class.getClassLoader().getResourceAsStream("pose pairings with backgrounds.tsv");
        if (inputStream == null) {
            System.out.println("Could not find pose pairings with backgrounds.tsv");
            return;
        }
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line = br.readLine(); //skip first line
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t", 5);
                String leftPose = tokens[0];
                List<String> combinedText = parseCell(tokens[1]);
                List<String> leftText = parseCell(tokens[2]);
                List<String> rightPose = parseCell(tokens[3]);
                List<String> backgrounds = parseCell(tokens[4]);
                vignetteSchemas.add(new VignetteSchema(leftPose, combinedText, leftText, rightPose, backgrounds));
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

    public static List<VignetteSchema> getVignetteSchemas() {
        return vignetteSchemas;
    }

    public static List<VignetteSchema> getVignetteSchemasInRange(int start, int end) {
        List<VignetteSchema> result = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            result.add(vignetteSchemas.get(i));
        }
        return result;
    }
}

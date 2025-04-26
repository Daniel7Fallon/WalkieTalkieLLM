package org.example.Vignette;

import org.example.Translation.Translator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VignetteManager {
    private static final List<VignetteSchema> vignetteSchemas = new ArrayList<>();
    static private final Random rand = new Random();

    //Deserialises pose pairings with backgrounds.tsv
    public static void initialize() {
        InputStream inputStream = VignetteManager.class.getClassLoader().getResourceAsStream("pose pairings with backgrounds.tsv");
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

    public static VignetteSchema getRandomVignetteSchema() {
        return (vignetteSchemas.isEmpty()) ? null : vignetteSchemas.get(rand.nextInt(vignetteSchemas.size()));
    }

    public static void translateAllVignetteSchemas() throws IOException {
        List<String> phrases = getLeftAndCombinedTexts(vignetteSchemas);
        Translator.batchTranslateList(phrases);
    }

    public static List<String> getLeftAndCombinedTexts(List<VignetteSchema> input) {
        List<String> result = new ArrayList<>();
        for(VignetteSchema vignetteSchema : input) {
            result.addAll(vignetteSchema.getLeftTexts());
            result.addAll(vignetteSchema.getCombinedTexts());
        }
        return result;
    }
}

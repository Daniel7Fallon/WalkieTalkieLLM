package org.example.Assets;

import java.util.List;
import java.util.Random;

public class VignetteSchema {
    static private Random rand = new Random();

    private String leftPose = null;
    private List<String> combinedTexts = null;
    private List<String> leftTexts = null;
    private List<String> rightPoses = null;
    private List<String> backgrounds = null;

    public VignetteSchema(String leftPose, List<String> combinedTexts, List<String> leftTexts, List<String> rightPoses, List<String> backgrounds) {
        this.leftPose = leftPose;
        this.combinedTexts = combinedTexts;
        this.leftTexts = leftTexts;
        this.rightPoses = rightPoses;
        this.backgrounds = backgrounds;
    }

    public Vignette getVignette() {
        String lP = leftPose;
        String cT = combinedTexts.get(rand.nextInt(combinedTexts.size()));
        String lT = leftTexts.get(rand.nextInt(leftTexts.size()));
        String rT = rightPoses.get(rand.nextInt(rightPoses.size()));
        String bT = backgrounds.get(rand.nextInt(backgrounds.size()));
        return new Vignette(lP, cT, lT, rT, bT);
    }

    public String getLeftPose() {
        return leftPose;
    }
    public List<String> getCombinedTexts() {
        return combinedTexts;
    }
    public List<String> getLeftTexts() {
        return leftTexts;
    }
    public List<String> getRightPoses() {
        return rightPoses;
    }
    public List<String> getBackgrounds() {
        return backgrounds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LP: " + leftPose + " | CT: " + combinedTexts + " | LT: " + leftTexts + " | RP: " + rightPoses + " | BG: " + backgrounds);
        return sb.toString();
    }
}

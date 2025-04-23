package org.example.Vignette;

import java.util.List;
import java.util.Random;

public class VignetteSchema {
    static private final Random rand = new Random();

    private final String leftPose;
    private final List<String> combinedTexts;
    private final List<String> leftTexts;
    private final List<String> rightPoses;
    private final List<String> backgrounds;

    public VignetteSchema(String leftPose, List<String> combinedTexts, List<String> leftTexts, List<String> rightPoses, List<String> backgrounds) {
        this.leftPose = leftPose;
        this.combinedTexts = combinedTexts;
        this.leftTexts = leftTexts;
        this.rightPoses = rightPoses;
        this.backgrounds = backgrounds;
    }

    public Vignette getRandVignette() {
        String cT = ((combinedTexts.isEmpty()) ? null : combinedTexts.get(rand.nextInt(combinedTexts.size())) );
        String lT = ((leftTexts.isEmpty()) ? null : leftTexts.get(rand.nextInt(leftTexts.size())) );
        String rT = ((rightPoses.isEmpty()) ? null : rightPoses.get(rand.nextInt(rightPoses.size())) );
        String bT = ((backgrounds.isEmpty()) ? null : backgrounds.get(rand.nextInt(backgrounds.size())) );
        return new Vignette(leftPose, cT, lT, rT, bT);
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
        return "LP: " + leftPose + " | CTs: " + combinedTexts + " | LTs: " + leftTexts + " | RPs: " + rightPoses + " | BGs: " + backgrounds;
    }
}

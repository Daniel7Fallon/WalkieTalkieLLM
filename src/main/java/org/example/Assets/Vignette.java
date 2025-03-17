package org.example.Assets;

import java.util.List;

public class Vignette {
    private String leftPose = null;
    private List<String> combinedText = null;
    private List<String> leftText = null;
    private List<String> rightPose = null;
    private List<String> backgrounds = null;

    public Vignette(String leftPose, List<String> combinedText, List<String> leftText, List<String> rightPose, List<String> backgrounds) {
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.backgrounds = backgrounds;
    }

    public String getLeftPose() {
        return leftPose;
    }
    public List<String> getCombinedText() {
        return combinedText;
    }
    public List<String> getLeftText() {
        return leftText;
    }
    public List<String> getRightPose() {
        return rightPose;
    }
    public List<String> getBackgrounds() {
        return backgrounds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LP: " + leftPose + " | CT: " + combinedText + " | LT: " + leftText + " | RP: " + rightPose + " | BG: " + backgrounds);
        return sb.toString();
    }
}

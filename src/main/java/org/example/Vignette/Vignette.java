package org.example.Vignette;

public class Vignette {
    private final String leftPose;
    private final String combinedText;
    private final String leftText;
    private final String rightPose;
    private final String background;

    public Vignette(String leftPose, String combinedText, String leftText, String rightPose, String background) {
        this.leftPose = leftPose;
        this.combinedText = combinedText;
        this.leftText = leftText;
        this.rightPose = rightPose;
        this.background = background;
    }

    public String getLeftPose() {
        return leftPose;
    }
    public String getCombinedText() {
        return combinedText;
    }
    public String getLeftText() {
        return leftText;
    }
    public String getRightPose() {
        return rightPose;
    }
    public String getBackgrounds() {
        return background;
    }

    public String toString() {
        return "LP: " + leftPose + " | CT: " + combinedText + " | LT: " + leftText + " | RP: " + rightPose + " | BG: " + background;
    }
}

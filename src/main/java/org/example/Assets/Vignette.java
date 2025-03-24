package org.example.Assets;

import java.util.List;

public class Vignette {
    private String leftPose = null;
    private String combinedText = null;
    private String leftText = null;
    private String rightPose = null;
    private String background = null;

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
}

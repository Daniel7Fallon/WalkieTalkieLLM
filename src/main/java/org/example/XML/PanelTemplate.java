package org.example.XML;

public enum PanelTemplate {
    // Both figures standing silently
    INTRO(
            false,  // left speaks?
            false  // right speaks?
    ),

    // Left figure speaks target, right figure silent
    LEFT_SPEAKS(
            true,   // left speaks?
            false  // right speaks?
    ),

    // Right figure speaks, left figure silent
    RIGHT_SPEAKS(
            false,  // left speaks?
            true   // right speaks?
    ),

    // Both figures speak
    BOTH_SPEAK(
            true,   // left speaks?
            true   // right speaks?
    );

    private final boolean leftSpeaks;
    private final boolean rightSpeaks;

    PanelTemplate(boolean leftSpeaks, boolean rightSpeaks) {
        this.leftSpeaks = leftSpeaks;
        this.rightSpeaks = rightSpeaks;
    }

    public boolean isLeftSpeaks() {
        return leftSpeaks;
    }

    public boolean isRightSpeaks() {
        return rightSpeaks;
    }

}
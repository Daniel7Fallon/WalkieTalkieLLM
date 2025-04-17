package org.example.Comic;

import org.example.XML.XMLParser;

public class Panel {
    private String above;
    private String below;
    private String border;
    private String setting;
    private PanelSide leftSide;
    private PanelSide middleSide;
    private PanelSide rightSide;
    private String audio;

    // Getters and setters

    public String getAbove() {
        return above;
    }
    public void setAbove(String above) {
        this.above = above;
    }
    public String getBelow() { return below; }
    public void setBelow(String below) { this.below = below; }
    public String getBorder() { return border; }
    public void setBorder(String border) { this.border = border; }
    public String getSetting() { return setting; }
    public void setSetting(String setting) { this.setting = setting; }
    public PanelSide getLeftSide() { return leftSide; }
    public void setLeftSide(PanelSide leftSide) { this.leftSide = leftSide; }
    public PanelSide getMiddleSide() { return middleSide; }
    public void setMiddleSide(PanelSide middleSide) { this.middleSide = middleSide; }
    public PanelSide getRightSide() { return rightSide; }
    public void setRightSide(PanelSide rightSide) { this.rightSide = rightSide; }
    public String getAudio() {
        return audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }

    public boolean hasLeft() {
        return leftSide != null;
    }
    public boolean hasMiddle() {
        return middleSide != null;
    }
    public boolean hasRight() {
        return rightSide != null;
    }

    public Panel deepCopy() {
        Panel panel = new Panel();
        panel.setAbove(this.above);
        panel.setBelow(this.below);
        panel.setBorder(this.border);
        panel.setSetting(this.setting);
        if(this.hasLeft()) panel.setLeftSide(this.leftSide.copy());
        if(this.hasMiddle()) panel.setMiddleSide(this.middleSide.copy());
        if(this.hasRight()) panel.setRightSide(this.rightSide.copy());
        panel.setAudio(this.audio);
        return panel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(above != null) sb.append("Above: " + above + "\n");
        if(below != null) sb.append("Below: " + below + "\n");
        if(border != null) sb.append("Border: " + border + "\n");
        if(setting != null) sb.append("Setting: " + setting + "\n");
        if(leftSide != null) sb.append("LeftSide: " + leftSide + "\n");
        if(middleSide != null) sb.append("MiddleSide: " + middleSide + "\n");
        if(rightSide != null) sb.append("RightSide: " + rightSide + "\n");
        return sb.toString();
    }
}

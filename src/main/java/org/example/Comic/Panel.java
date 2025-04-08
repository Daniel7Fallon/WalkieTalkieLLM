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

    public boolean hasLeft() {
        return leftSide != null;
    }
    public boolean hasMiddle() {
        return middleSide != null;
    }
    public boolean hasRight() {
        return rightSide != null;
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

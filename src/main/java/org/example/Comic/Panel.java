package org.example.Comic;

import org.example.XML.XMLParser;

public class Panel {
    private String below;
    private String border;
    private String setting;
    private PanelSide leftSide;
    private PanelSide middleSide;
    private PanelSide rightSide;

    // Getters and setters
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
}

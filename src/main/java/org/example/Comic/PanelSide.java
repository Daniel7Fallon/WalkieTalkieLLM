package org.example.Comic;

import org.example.XML.XMLParser;

public class PanelSide {
    private PanelFigure panelFigure;
    private String ballonStatus;
    private String balloonContent;

    // Getters and setters
    public PanelFigure getPanelFigure() { return panelFigure; }
    public void setPanelFigure(PanelFigure panelFigure) { this.panelFigure = panelFigure; }
    public String getBalloonStatus() {
        return ballonStatus;
    }
    public void setBallonStatus(String ballonStatus) {
        this.ballonStatus = ballonStatus;
    }
    public String getBalloonContent() {
        return balloonContent;
    }
    public void setBalloonContent(String balloonContent) {
        this.balloonContent = balloonContent;
    }
}

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(panelFigure != null) sb.append(" -PanelFigure: " + panelFigure);
        if(ballonStatus != null) sb.append(" -BallonStatus: " + ballonStatus);
        if(balloonContent != null) sb.append(" -BalloonContent: " + balloonContent);
        return sb.toString();
    }
}

package org.example.Comic;

import org.example.Comic.Dialogue.CharacterDialogue;

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
    public void setBalloonStatus(String ballonStatus) {
        this.ballonStatus = ballonStatus;
    }
    public String getBalloonContent() {
        return balloonContent;
    }
    public void setBalloonContent(String balloonContent) {
        this.balloonContent = balloonContent;
    }

    public boolean hasBalloonContent() {
        return balloonContent != null;
    }

    public boolean hasCharacter() {
        return this.getPanelFigure() != null && this.getPanelFigure().getName() != null;
    }

    public void attemptReplaceDialogue(CharacterDialogue characterDialogue) {
        if(this.hasCharacter() && this.panelFigure.getName().equals(characterDialogue.getName())) {
            this.setBalloonStatus("speech");
            this.setBalloonContent(characterDialogue.getContent());
        }
    }

    public String getAudiovisualDescription() {
        if(this.getPanelFigure() != null && this.getPanelFigure().getName() != null) {
            return this.getPanelFigure().getName() + " is " + this.getBalloonContent() + ".";
        }
        return "";
    }

    public PanelSide copy() {
        PanelSide panelSide = new PanelSide();
        panelSide.panelFigure = this.panelFigure;
        panelSide.ballonStatus = this.ballonStatus;
        panelSide.balloonContent = this.balloonContent;
        return panelSide;
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

package org.example.Comic;

public class PanelFigure {
    private Figure figure;

    private String pose;
    private String horizontal;
    private String vertical;

    public PanelFigure(Figure figure) {
        this.figure = figure;
    }

    public PanelFigure(Figure figure, String pose, String horizontal, String vertical) {
        this.figure = figure;
        this.pose = pose;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }


    public String getId() {
        return figure.getId();
    }
    public void setId(String id) {
        this.figure.setId(id);
    }
    public String getName() {
        return figure.getName();
    }
    public void setName(String name) {
        this.figure.setName(name);
    }
    public String getAppearance() {
        return figure.getAppearance();
    }
    public void setAppearance(String appearance) {
        this.figure.setAppearance(appearance);
    }
    public String getSkin() {
        return figure.getSkin();
    }
    public void setSkin(String skin) {
        this.figure.setSkin(skin);
    }
    public String getHair() {
        return figure.getHair();
    }
    public void setHair(String hair) {
        this.figure.setHair(hair);
    }
    public String getBeard() {
        return figure.getBeard();
    }
    public void setBeard(String beard) {
        this.figure.setBeard(beard);
    }
    public String getHairLength() {
        return figure.getHairLength();
    }
    public void setHairLength(String hairLength) {
        this.figure.setHairLength(hairLength);
    }
    public String getHairStyle() {
        return figure.getHairStyle();
    }
    public void setHairStyle(String hairStyle) {
        this.figure.setHairStyle(hairStyle);
    }
    public String getLips() {
        return figure.getLips();
    }
    public void setLips(String lips) {
        this.figure.setLips(lips);
    }
    public String getFacing() {
        return figure.getFacing();
    }
    public void setFacing(String facing) {
        this.figure.setFacing(facing);
    }

    public String getPose() { return pose; }
    public void setPose(String pose) { this.pose = pose; }
    public String getHorizontal() { return horizontal; }
    public void setHorizontal(String horizontal) { this.horizontal = horizontal; }
    public String getVertical() { return vertical; }
    public void setVertical(String vertical) { this.vertical = vertical; }
}

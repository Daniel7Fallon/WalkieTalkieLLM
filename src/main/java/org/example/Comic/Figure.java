package org.example.Comic;

public class Figure {
    private String name;
    private String appearance = null;
    private String skin = null;
    private String hair = null;
    private String beard = null;
    private String hairLength = null;
    private String hairStyle = null;
    private String lips = null;
    //pose and facing come from vignette

    public Figure() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAppearance() {
        return appearance;
    }
    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }
    public String getSkin() {
        return skin;
    }
    public void setSkin(String skin) {
        this.skin = skin;
    }
    public String getHair() {
        return hair;
    }
    public void setHair(String hair) {
        this.hair = hair;
    }
    public String getBeard() {
        return beard;
    }
    public void setBeard(String beard) {
        this.beard = beard;
    }
    public String getHairLength() {
        return hairLength;
    }
    public void setHairLength(String hairLength) {
        this.hairLength = hairLength;
    }
    public String getHairStyle() {
        return hairStyle;
    }
    public void setHairStyle(String hairStyle) {
        this.hairStyle = hairStyle;
    }
    public String getLips() {
        return lips;
    }
    public void setLips(String lips) {
        this.lips = lips;
    }
}

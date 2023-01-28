package com.example.twittokandroid.Repositories;

public class Twok {

    private String sid;
    private Integer uid;
    private String name;
    private String text;
    private String bgcol;
    private String fontcol;
    private Integer pversion;
    private Integer fontsize;
    private Integer fonttype;
    private Integer halign;
    private Integer valign;
    private Double lat;
    private Double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Twok() {
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getPversion() {
        return pversion;
    }

    public void setPversion(Integer pversion) {
        this.pversion = pversion;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBgcol() {
        return bgcol;
    }

    public void setBgcol(String bgcol) {
        this.bgcol = bgcol;
    }

    public String getFontcol() {
        return fontcol;
    }

    public void setFontcol(String fontcol) {
        this.fontcol = fontcol;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public Integer getFonttype() {
        return fonttype;
    }

    public void setFonttype(Integer fonttype) {
        this.fonttype = fonttype;
    }

    public Integer getHalign() {
        return halign;
    }

    public void setHalign(Integer halign) {
        this.halign = halign;
    }

    public Integer getValign() {
        return valign;
    }

    public void setValign(Integer valign) {
        this.valign = valign;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}

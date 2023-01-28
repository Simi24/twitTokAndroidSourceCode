package com.example.twittokandroid.Repositories;

public class Picture {

    private Integer uid;
    private String name;
    private Integer pversion;
    private String picture;

    public Picture() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPversion() {
        return pversion;
    }

    public void setPversion(Integer pversion) {
        this.pversion = pversion;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

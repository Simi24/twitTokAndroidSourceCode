package com.example.twittokandroid.Repositories;

public class Profile {
    private Integer uid;
    private String sid;
    private String name;
    private String picture;
    private Integer pversion;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getPversion() {
        return pversion;
    }

    public void setPversion(Integer pversion) {
        this.pversion = pversion;
    }
}

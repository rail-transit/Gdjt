package com.example.passenger.entity;

import java.io.Serializable;

public class StyleContent implements Serializable {
    private Integer id;
    private Integer styleID;
    private String layoutID;
    private Integer contentID;
    private Integer materialID;
    private Integer timeLength;
    private Integer playtimes;
    private String fileproterty;
    private String getcontents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStyleID() {
        return styleID;
    }

    public void setStyleID(Integer styleID) {
        this.styleID = styleID;
    }

    public String getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }

    public Integer getContentID() {
        return contentID;
    }

    public void setContentID(Integer contentID) {
        this.contentID = contentID;
    }

    public Integer getMaterialID() {
        return materialID;
    }

    public void setMaterialID(Integer materialID) {
        this.materialID = materialID;
    }

    public Integer getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
    }

    public Integer getPlaytimes() {
        return playtimes;
    }

    public void setPlaytimes(Integer playtimes) {
        this.playtimes = playtimes;
    }

    public String getFileproterty() {
        return fileproterty;
    }

    public void setFileproterty(String fileproterty) {
        this.fileproterty = fileproterty;
    }

    public String getGetcontents() {
        return getcontents;
    }

    public void setGetcontents(String getcontents) {
        this.getcontents = getcontents;
    }
}

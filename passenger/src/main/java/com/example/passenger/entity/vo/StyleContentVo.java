package com.example.passenger.entity.vo;

import java.io.Serializable;

public class StyleContentVo implements Serializable {
    private Integer id;
    private Integer timeLength;
    private String fodderName;
    private Integer styleID;
    private Integer materialID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(Integer timeLength) {
        this.timeLength = timeLength;
    }

    public String getFodderName() {
        return fodderName;
    }

    public void setFodderName(String fodderName) {
        this.fodderName = fodderName;
    }

    public Integer getStyleID() {
        return styleID;
    }

    public void setStyleID(Integer styleID) {
        this.styleID = styleID;
    }

    public Integer getMaterialID() {
        return materialID;
    }

    public void setMaterialID(Integer materialID) {
        this.materialID = materialID;
    }
}

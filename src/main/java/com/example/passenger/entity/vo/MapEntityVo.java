package com.example.passenger.entity.vo;

import java.io.Serializable;

public class MapEntityVo implements Serializable {
    private String id;
    private String divTop;
    private String divLef;
    private String equipmentID;
    private String equipmentName;
    private String mapImg;
    private String left;
    private String top;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDivTop() {
        return divTop;
    }

    public void setDivTop(String divTop) {
        this.divTop = divTop;
    }

    public String getDivLef() {
        return divLef;
    }

    public void setDivLef(String divLef) {
        this.divLef = divLef;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getMapImg() {
        return mapImg;
    }

    public void setMapImg(String mapImg) {
        this.mapImg = mapImg;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }
}

package com.example.passenger.entity;

import java.io.Serializable;

public class LineStandby implements Serializable {
    private Integer id;
    private Integer lineID;
    private Integer standbyType;
    private Integer availableNumber;
    private Integer faultNumber;
    private Integer reworkingNumber;
    private Integer scrapNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLineID() {
        return lineID;
    }

    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }

    public Integer getStandbyType() {
        return standbyType;
    }

    public void setStandbyType(Integer standbyType) {
        this.standbyType = standbyType;
    }

    public Integer getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(Integer availableNumber) {
        this.availableNumber = availableNumber;
    }

    public Integer getFaultNumber() {
        return faultNumber;
    }

    public void setFaultNumber(Integer faultNumber) {
        this.faultNumber = faultNumber;
    }

    public Integer getReworkingNumber() {
        return reworkingNumber;
    }

    public void setReworkingNumber(Integer reworkingNumber) {
        this.reworkingNumber = reworkingNumber;
    }

    public Integer getScrapNumber() {
        return scrapNumber;
    }

    public void setScrapNumber(Integer scrapNumber) {
        this.scrapNumber = scrapNumber;
    }
}

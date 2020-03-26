package com.example.passenger.entity;

import java.io.Serializable;

public class DevRepair implements Serializable {
    private Integer id;
    private Integer deviceID;
    private String accendant;
    private String tel;
    private String startTime;
    private String endTime;
    private String note;
    private String scheme;
    private Integer repairResult;
    private Integer stationID;
    private Integer lineID;
    private String repairPeople;
    private String repairTel;

    public String getRepairTel() {
        return repairTel;
    }

    public void setRepairTel(String repairTel) {
        this.repairTel = repairTel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getAccendant() {
        return accendant;
    }

    public void setAccendant(String accendant) {
        this.accendant = accendant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Integer getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(Integer repairResult) {
        this.repairResult = repairResult;
    }

    public Integer getStationID() {
        return stationID;
    }

    public void setStationID(Integer stationID) {
        this.stationID = stationID;
    }

    public Integer getLineID() {
        return lineID;
    }

    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }

    public String getRepairPeople() {
        return repairPeople;
    }

    public void setRepairPeople(String repairPeople) {
        this.repairPeople = repairPeople;
    }
}

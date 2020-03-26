package com.example.passenger.entity.vo;

import java.io.Serializable;

public class DevRepairVo implements Serializable {
    private Integer id;
    private Integer deviceID;
    private String accendant;
    private String tel;
    private String startTime;
    private String endTime;
    private String note;
    private String scheme;
    private Integer repairResult;
    private String repairPeople;
    private String repairTel;
    private Integer stationID;
    private Integer lineID;
    private String lineName;
    private String stationName;
    private String deviceName;
    private Integer type;

    public String getRepairTel() {
        return repairTel;
    }

    public void setRepairTel(String repairTel) {
        this.repairTel = repairTel;
    }

    public String getRepairPeople() {
        return repairPeople;
    }

    public void setRepairPeople(String repairPeople) {
        this.repairPeople = repairPeople;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

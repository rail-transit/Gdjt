package com.example.passenger.entity;

import java.io.Serializable;

public class DevUpkeep implements Serializable {
    private Integer id;
    private Integer deviceID;
    private String accendant;
    private String tel;
    private String startTime;
    private String endTime;
    private String scheme;
    private Integer upkeepState;
    private Integer stationID;
    private Integer lineID;

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

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Integer getUpkeepState() {
        return upkeepState;
    }

    public void setUpkeepState(Integer upkeepState) {
        this.upkeepState = upkeepState;
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
}

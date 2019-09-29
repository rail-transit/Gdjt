package com.example.passenger.entity;

import java.io.Serializable;

public class Station implements Serializable {
    private Integer id;
    private String stationID;
    private String name;
    private String enName;
    private String des;
    private String serverIP;
    private Integer isTrain;
    private Integer isCenter;
    private String upStartTime;
    private String upStopTime;
    private String downStartTime;
    private String downStopTime;
    private Integer lineID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Integer getIsTrain() {
        return isTrain;
    }

    public void setIsTrain(Integer isTrain) {
        this.isTrain = isTrain;
    }

    public Integer getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(Integer isCenter) {
        this.isCenter = isCenter;
    }

    public String getUpStartTime() {
        return upStartTime;
    }

    public void setUpStartTime(String upStartTime) {
        this.upStartTime = upStartTime;
    }

    public String getUpStopTime() {
        return upStopTime;
    }

    public void setUpStopTime(String upStopTime) {
        this.upStopTime = upStopTime;
    }

    public String getDownStartTime() {
        return downStartTime;
    }

    public void setDownStartTime(String downStartTime) {
        this.downStartTime = downStartTime;
    }

    public String getDownStopTime() {
        return downStopTime;
    }

    public void setDownStopTime(String downStopTime) {
        this.downStopTime = downStopTime;
    }

    public Integer getLineID() {
        return lineID;
    }

    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationID='" + stationID + '\'' +
                ", name='" + name + '\'' +
                ", enName='" + enName + '\'' +
                ", des='" + des + '\'' +
                ", serverIP='" + serverIP + '\'' +
                ", isTrain=" + isTrain +
                ", isCenter=" + isCenter +
                ", upStartTime='" + upStartTime + '\'' +
                ", upStopTime='" + upStopTime + '\'' +
                ", downStartTime='" + downStartTime + '\'' +
                ", downStopTime='" + downStopTime + '\'' +
                ", lineID=" + lineID +
                '}';
    }
}

package com.example.passenger.entity;

import java.io.Serializable;

public class DevAlarmLog implements Serializable {
    private Integer id;
    private String deviceID;
    private String deviceName;
    private String alarmType;
    private String spotID;
    private String spotName;
    private String value;
    private String event;
    private String timeStamp;
    private String stationID;
    private String lineID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    @Override
    public String toString() {
        return "DevAlarmLog{" +
                "id=" + id +
                ", deviceID='" + deviceID + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", spotID='" + spotID + '\'' +
                ", spotName='" + spotName + '\'' +
                ", value='" + value + '\'' +
                ", event='" + event + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", stationID='" + stationID + '\'' +
                ", lineID='" + lineID + '\'' +
                '}';
    }
}

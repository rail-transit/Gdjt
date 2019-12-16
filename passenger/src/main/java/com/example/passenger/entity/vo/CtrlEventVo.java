package com.example.passenger.entity.vo;

import java.io.Serializable;

public class CtrlEventVo implements Serializable {
    private Integer id;
    private Integer deviceID;
    private String ctrlType;
    private String ctrlTime;
    private String ctrlResult;
    private String note;
    private Integer stationID;
    private Integer lineID;
    private String lineName;
    private String stationName;
    private String deviceName;
    private String devicePosName;
    private String ip;
    private String mac;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevicePosName() {
        return devicePosName;
    }

    public void setDevicePosName(String devicePosName) {
        this.devicePosName = devicePosName;
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

    public String getCtrlType() {
        return ctrlType;
    }

    public void setCtrlType(String ctrlType) {
        this.ctrlType = ctrlType;
    }

    public String getCtrlTime() {
        return ctrlTime;
    }

    public void setCtrlTime(String ctrlTime) {
        this.ctrlTime = ctrlTime;
    }

    public String getCtrlResult() {
        return ctrlResult;
    }

    public void setCtrlResult(String ctrlResult) {
        this.ctrlResult = ctrlResult;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

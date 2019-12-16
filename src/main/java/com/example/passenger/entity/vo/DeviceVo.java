package com.example.passenger.entity.vo;

import java.io.Serializable;

public class DeviceVo implements Serializable {
    private Integer id;
    private String deviceID;
    private String name;
    private String des;
    private Integer type;
    private Integer devicePosID;
    private String ip;
    private String mac;
    private Integer stationID;
    private Integer lineID;
    private String  param;
    private String resolution;
    private String isBackups;
    private String stationName;
    private String lineName;
    private String devicePosName;

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

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDevicePosID() {
        return devicePosID;
    }

    public void setDevicePosID(Integer devicePosID) {
        this.devicePosID = devicePosID;
    }

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getIsBackups() {
        return isBackups;
    }

    public void setIsBackups(String isBackups) {
        this.isBackups = isBackups;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}

package com.example.passenger.entity;

import java.io.Serializable;

public class OperationLog implements Serializable {
    private Integer id;
    private String operator;
    private String type;
    private String content;
    private String time;
    private Integer isExported;
    private Integer deviceID;
    private Integer stationID;
    private Integer lineID;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getIsExported() {
        return isExported;
    }

    public void setIsExported(Integer isExported) {
        this.isExported = isExported;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
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

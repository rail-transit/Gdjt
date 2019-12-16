package com.example.passenger.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private Integer id;
    private String msg;
    private Integer state;
    private Integer playState;
    private String level;
    private Integer type;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private Integer deviceID;
    private String ctrlID;
    private String operator;
    private Integer isPlanMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPlayState() {
        return playState;
    }

    public void setPlayState(Integer playState) {
        this.playState = playState;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    public String getCtrlID() {
        return ctrlID;
    }

    public void setCtrlID(String ctrlID) {
        this.ctrlID = ctrlID;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getIsPlanMsg() {
        return isPlanMsg;
    }

    public void setIsPlanMsg(Integer isPlanMsg) {
        this.isPlanMsg = isPlanMsg;
    }
}

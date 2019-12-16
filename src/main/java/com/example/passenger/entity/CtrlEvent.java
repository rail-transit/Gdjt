package com.example.passenger.entity;

public class CtrlEvent {
    private Integer id;
    private Integer deviceID;
    private String ctrlType;
    private String ctrlTime;
    private String ctrlResult;
    private String note;
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
}

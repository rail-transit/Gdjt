package com.example.passenger.entity;

import java.io.Serializable;

public class PlayListScope implements Serializable {
    private char listID;
    private char devicePos;
    private char stationCode;
    private char lineCode;

    public char getListID() {
        return listID;
    }

    public void setListID(char listID) {
        this.listID = listID;
    }

    public char getDevicePos() {
        return devicePos;
    }

    public void setDevicePos(char devicePos) {
        this.devicePos = devicePos;
    }

    public char getStationCode() {
        return stationCode;
    }

    public void setStationCode(char stationCode) {
        this.stationCode = stationCode;
    }

    public char getLineCode() {
        return lineCode;
    }

    public void setLineCode(char lineCode) {
        this.lineCode = lineCode;
    }
}

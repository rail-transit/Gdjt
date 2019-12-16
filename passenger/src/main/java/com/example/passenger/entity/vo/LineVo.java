package com.example.passenger.entity.vo;


import java.io.Serializable;
import java.util.List;

public class LineVo implements Serializable {
    private Integer id;
    private String lineID;
    private String name;
    private String enName;
    private String serverIP;
    private String des;
    private List<StationVo> stationVoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
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

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<StationVo> getStationVoList() {
        return stationVoList;
    }

    public void setStationVoList(List<StationVo> stationVoList) {
        this.stationVoList = stationVoList;
    }

    @Override
    public String toString() {
        return "LineVo{" +
                "id=" + id +
                ", lineID='" + lineID + '\'' +
                ", name='" + name + '\'' +
                ", enName='" + enName + '\'' +
                ", serverIP='" + serverIP + '\'' +
                ", des='" + des + '\'' +
                ", stationVoList=" + stationVoList +
                '}';
    }
}

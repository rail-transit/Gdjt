package com.example.passenger.entity;

import java.io.Serializable;

public class DeviceType implements Serializable {
    private Integer id;
    private String name;
    private String dllName;
    private Integer isCtrl;
    private Integer hasVolume;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDllName() {
        return dllName;
    }

    public void setDllName(String dllName) {
        this.dllName = dllName;
    }

    public Integer getIsCtrl() {
        return isCtrl;
    }

    public void setIsCtrl(Integer isCtrl) {
        this.isCtrl = isCtrl;
    }

    public Integer getHasVolume() {
        return hasVolume;
    }

    public void setHasVolume(Integer hasVolume) {
        this.hasVolume = hasVolume;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "DeviceType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dllName='" + dllName + '\'' +
                ", isCtrl=" + isCtrl +
                ", hasVolume=" + hasVolume +
                ", note='" + note + '\'' +
                '}';
    }
}

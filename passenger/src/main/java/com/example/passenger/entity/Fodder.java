package com.example.passenger.entity;

import java.io.Serializable;
import java.util.Date;

public class Fodder implements Serializable {
    private Integer id;
    private String name;
    private Integer type;
    private String path;
    private String size;
    private Integer state;
    private String editorID;
    private Date editTime;
    private String resolution;
    private String timeLength;
    private String note;
    private String guid;
    private String md5;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEditorID() {
        return editorID;
    }

    public void setEditorID(String editorID) {
        this.editorID = editorID;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "Fodder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", state=" + state +
                ", editorID='" + editorID + '\'' +
                ", editTime=" + editTime +
                ", resolution='" + resolution + '\'' +
                ", timeLength='" + timeLength + '\'' +
                ", note='" + note + '\'' +
                ", guid='" + guid + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}

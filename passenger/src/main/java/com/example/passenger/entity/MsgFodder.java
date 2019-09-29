package com.example.passenger.entity;

import java.io.Serializable;

public class MsgFodder implements Serializable {
    private Integer id;
    private String title;
    private Integer type;
    private Integer state;
    private String contentCN;
    private String contentEN;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getContentCN() {
        return contentCN;
    }

    public void setContentCN(String contentCN) {
        this.contentCN = contentCN;
    }

    public String getContentEN() {
        return contentEN;
    }

    public void setContentEN(String contentEN) {
        this.contentEN = contentEN;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

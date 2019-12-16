package com.example.passenger.entity;

import java.io.Serializable;

public class StandbyType implements Serializable {
    private Integer id;
    private String standbyName;
    private Integer minNumber;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStandbyName() {
        return standbyName;
    }

    public void setStandbyName(String standbyName) {
        this.standbyName = standbyName;
    }

    public Integer getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

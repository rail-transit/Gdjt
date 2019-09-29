package com.example.passenger.entity;

import java.io.Serializable;

public class DevicePos implements Serializable {
    private Integer id;
    private String name;
    private String note;
    private Integer isEdit;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public String toString() {
        return "DevicePos{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", note='" + note + '\'' +
                ", isEdit=" + isEdit +
                '}';
    }
}

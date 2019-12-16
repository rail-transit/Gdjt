package com.example.passenger.entity;

import java.io.Serializable;

public class FodderType implements Serializable {
    private Integer id;
    private Integer type;
    private String suffixName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    @Override
    public String toString() {
        return "FodderType{" +
                "id=" + id +
                ", type=" + type +
                ", suffixName='" + suffixName + '\'' +
                '}';
    }
}

package com.example.passenger.entity;

import java.io.Serializable;

public class DeviceSpot implements Serializable {
    private Integer id;
    private String name;
    private String parameter;
    private Integer type;
    private Integer min;
    private Integer max;
    private String value;
    private Integer deviceType;
    private Integer isShow;
    private Integer ctrlType;

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

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getCtrlType() {
        return ctrlType;
    }

    public void setCtrlType(Integer ctrlType) {
        this.ctrlType = ctrlType;
    }

    @Override
    public String toString() {
        return "DeviceSpot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                ", type=" + type +
                ", min=" + min +
                ", max=" + max +
                ", value='" + value + '\'' +
                ", deviceType=" + deviceType +
                ", isShow=" + isShow +
                ", ctrlType=" + ctrlType +
                '}';
    }
}

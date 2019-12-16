package com.example.passenger.entity;

import java.io.Serializable;

public class GroupRight implements Serializable {
    private Integer GroupID;
    private Integer FirstRight;
    private Integer SecondRight;
    private Integer ThirdRight;
    private Integer LineID;
    private Integer StationID;

    public Integer getGroupID() {
        return GroupID;
    }

    public void setGroupID(Integer groupID) {
        GroupID = groupID;
    }

    public Integer getFirstRight() {
        return FirstRight;
    }

    public void setFirstRight(Integer firstRight) {
        FirstRight = firstRight;
    }

    public Integer getSecondRight() {
        return SecondRight;
    }

    public void setSecondRight(Integer secondRight) {
        SecondRight = secondRight;
    }

    public Integer getThirdRight() {
        return ThirdRight;
    }

    public void setThirdRight(Integer thirdRight) {
        ThirdRight = thirdRight;
    }

    public Integer getLineID() {
        return LineID;
    }

    public void setLineID(Integer lineID) {
        LineID = lineID;
    }

    public Integer getStationID() {
        return StationID;
    }

    public void setStationID(Integer stationID) {
        StationID = stationID;
    }

    @Override
    public String toString() {
        return "GroupRight{" +
                "GroupID=" + GroupID +
                ", FirstRight=" + FirstRight +
                ", SecondRight=" + SecondRight +
                ", ThirdRight=" + ThirdRight +
                ", LineID=" + LineID +
                ", StationID=" + StationID +
                '}';
    }
}

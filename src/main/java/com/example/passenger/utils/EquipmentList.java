package com.example.passenger.utils;

import java.util.Timer;
import java.util.TimerTask;

public class EquipmentList {
    private String deviceID;
    private String routingKey;
    private Integer count;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public void setTimer(Timer timer) {
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                count++;
            }
        }, 0,1000);
    }

}

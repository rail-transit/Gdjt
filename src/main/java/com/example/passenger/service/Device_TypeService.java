package com.example.passenger.service;

import com.example.passenger.entity.Device_Type;
import com.example.passenger.mapper.Device_TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Device_TypeService {
    private static final Logger logger = LoggerFactory.getLogger(Device_TypeService.class);

    @Autowired
    Device_TypeMapper device_typeMapper;

    public List<Device_Type> selectAllDeviceType(){
        return device_typeMapper.selectAllDeviceType();
    }
}

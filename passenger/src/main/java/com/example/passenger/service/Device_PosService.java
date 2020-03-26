package com.example.passenger.service;

import com.example.passenger.entity.Device_Pos;
import com.example.passenger.mapper.Device_PosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Device_PosService {
    private static final Logger logger = LoggerFactory.getLogger(Device_PosService.class);

    @Autowired
    Device_PosMapper device_posMapper;

    public List<Device_Pos> selectAllDevicePos() {
        return device_posMapper.selectAllDevicePos();
    }
}

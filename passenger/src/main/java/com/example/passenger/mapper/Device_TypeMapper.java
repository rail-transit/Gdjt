package com.example.passenger.mapper;

import com.example.passenger.entity.Device_Type;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Device_TypeMapper {
    List<Device_Type> selectAllDeviceType();
}

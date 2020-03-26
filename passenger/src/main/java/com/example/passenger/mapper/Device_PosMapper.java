package com.example.passenger.mapper;

import com.example.passenger.entity.Device_Pos;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Device_PosMapper {
    List<Device_Pos> selectAllDevicePos();
}

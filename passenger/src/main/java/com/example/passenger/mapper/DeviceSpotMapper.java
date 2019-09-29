package com.example.passenger.mapper;

import com.example.passenger.entity.DeviceSpot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceSpotMapper {

    List<DeviceSpot> selectDeviceSpot(@Param("type") Integer type);

    List<DeviceSpot> selectAllDeviceSpot();

    List<DeviceSpot> selectDeviceSpotPaging(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count();

    Integer addDeviceSpot(DeviceSpot deviceSpot);

    Integer updateDeviceSpot(DeviceSpot deviceSpot);

    Integer deleteDeviceSpot(Integer id);
}

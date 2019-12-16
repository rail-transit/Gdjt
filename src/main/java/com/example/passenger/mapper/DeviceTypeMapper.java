package com.example.passenger.mapper;

import com.example.passenger.entity.DeviceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceTypeMapper {
    DeviceType selectDeviceTypeById(@Param("id") Integer id);

    DeviceType selectTypeByName();

    List<DeviceType> selectAllDeviceType();

    List<DeviceType> selectDeviceTypePaging(@Param("pageNum") Integer pageNum,@Param("pageSize")Integer pageSize);

    Integer count();

    Integer selectDeviceTypeByName(@Param("name") String name,
                                   @Param("id") Integer id);

    Integer addDeviceType(DeviceType device_type);

    Integer updateDeviceType(DeviceType device_type);

    Integer deleteDeviceType(Integer id);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.DevicePos;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevicePosMapper {

    DevicePos selectDevicePosById(@Param("id") Integer id);

    Integer selectDevicePosByName(@Param("name") String name,
                                  @Param("id") Integer id);

    List<DevicePos> selectAllDevicePos();

    List<DevicePos> selectDevicePosPaging(@Param("pageNum")Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count();

    Integer addDevicePos(DevicePos device_pos);

    Integer updateDevicePos(DevicePos device_pos);

    Integer deleteDevicePos(Integer id);

}

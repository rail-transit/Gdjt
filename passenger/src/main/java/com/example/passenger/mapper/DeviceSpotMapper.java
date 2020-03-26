package com.example.passenger.mapper;

import com.example.passenger.entity.DeviceSpot;
import com.example.passenger.entity.vo.DeviceSpotVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceSpotMapper {

    DeviceSpot selectDeviceSpotById(Integer id);

    DeviceSpot selectDeviceSpotByCtrlType(@Param("id") Integer id,
                                          @Param("type") Integer type,
                                          @Param("ctrlType") Integer ctrlType);

    List<DeviceSpot> selectDeviceSpot(@Param("type") Integer type);

    List<DeviceSpot> selectAllDeviceSpot();

    List<DeviceSpotVo> selectDeviceSpotPaging(@Param("deviceType") Integer deviceType,
                                              @Param("pageNum") Integer pageNum,
                                              @Param("pageSize") Integer pageSize);

    Integer count(@Param("deviceType") Integer deviceType);

    Integer selectDeviceSpotByName(@Param("deviceType") Integer deviceType,
                                   @Param("name") String name,
                                   @Param("id") Integer id);

    Integer addDeviceSpot(DeviceSpot deviceSpot);

    Integer updateDeviceSpot(DeviceSpot deviceSpot);

    Integer deleteDeviceSpot(Integer id);
}

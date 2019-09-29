package com.example.passenger.mapper;

import com.example.passenger.entity.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper {

    Device selectDevice(Integer id);

    Device selectDeviceByIp(String ip);

    List<Device> queryAllDevice();

    List<Device> selectAllDevice(Integer stationID);

    List<Device> selectDevicePaging(@Param("stationID")Integer stationID,@Param("type")Integer type,
                                    @Param("pageNum")Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count(@Param("stationID")Integer stationID,@Param("type")Integer type);

    Integer addDevice(Device device);

    Integer updateDevice(Device device);

    Integer deleteDevice(Integer id);

    Integer deleteDeviceByLineId(Integer lineID);

    Integer deleteDeviceByStationId(Integer stationID);
}

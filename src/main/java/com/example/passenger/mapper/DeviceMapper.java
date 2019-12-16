package com.example.passenger.mapper;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.vo.DeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper {

    Device selectDevice(Integer id);

    List<Device> selectDeviceByType(@Param("stationID") Integer stationID,
                                    @Param("type") Integer type);

    Device selectDeviceByIp(String ip);

    List<Device> selectDeviceById(@Param("lineID") Integer lineID,
                                  @Param("stationID") Integer stationID,
                                  @Param("type") Integer type);

    List<Device> getDeviceList(@Param("lineID") Integer lineID,
                               @Param("stationID") Integer stationID,
                               @Param("id") Integer id);

    List<Device> queryAllDevice();

    List<DeviceVo> queryAllDeviceVo();

    List<Device> selectAllDevice(Integer stationID);

    List<DeviceVo> selectDevicePaging(@Param("lineID") Integer lineID,
                                      @Param("stationID")Integer stationID,
                                      @Param("type")Integer type,
                                      @Param("id")Integer id,
                                      @Param("pageNum")Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer count(@Param("lineID") Integer lineID,
                  @Param("stationID")Integer stationID,
                  @Param("type")Integer type,
                  @Param("id")Integer id);

    Integer selectDeviceByName(@Param("lineID") Integer lineID,
                               @Param("stationID") Integer stationID,
                               @Param("deviceID") String deviceID,
                               @Param("name") String name,
                               @Param("id") Integer id);

    Integer addDevice(Device device);

    Integer updateDevice(Device device);

    Integer deleteDevice(Integer id);

    Integer deleteDeviceByLineId(Integer lineID);

    Integer deleteDeviceByStationId(Integer stationID);
}

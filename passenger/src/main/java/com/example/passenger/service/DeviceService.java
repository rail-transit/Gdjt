package com.example.passenger.service;

import com.example.passenger.entity.Device;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(LineService.class);

    @Autowired
    DeviceMapper deviceMapper;

    public Device selectDeviceByIp(String ip){
        return deviceMapper.selectDeviceByIp(ip);
    }

    public Device selectDevice(Integer id){
        return deviceMapper.selectDevice(id);
    }

    public List<Device> queryAllDevice(){
       return deviceMapper.queryAllDevice();
    }

    public List<Device> selectAllDevice(Integer stationID){
        return deviceMapper.selectAllDevice(stationID);
    }

    public PageUtil selectDevicePaging(Integer stationID,Integer type,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(deviceMapper.count(stationID,type));
        pageUtil.setPageData(deviceMapper.selectDevicePaging(stationID,type,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevice(Device device){
        try {
            return deviceMapper.addDevice(device);
        }catch (Exception e){
            logger.error("添加设备异常",e.toString());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer updateDevice(Device device){
        try {
            return deviceMapper.updateDevice(device);
        }catch (Exception e){
            logger.error("添加设备异常",e.toString());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteDevice(Integer id){
        try {
            return deviceMapper.deleteDevice(id);
        }catch (Exception e){
            logger.error("添加设备异常",e.toString());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteDeviceByLineId(Integer lineID){
        try {
            return deviceMapper.deleteDeviceByLineId(lineID);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDeviceByStationId(Integer stationID){
        try {
            return deviceMapper.deleteDeviceByStationId(stationID);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

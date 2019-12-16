package com.example.passenger.service;

import com.example.passenger.entity.Device;
import com.example.passenger.entity.vo.DeviceVo;
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

    public List<DeviceVo> queryAllDeviceVo(){
        return deviceMapper.queryAllDeviceVo();
    }

    public Device selectDevice(Integer id){
        return deviceMapper.selectDevice(id);
    }

    public List<Device> queryAllDevice(){
       return deviceMapper.queryAllDevice();
    }

    public List<Device> selectDeviceByType(Integer stationID,Integer type){
        return deviceMapper.selectDeviceByType(stationID,type);
    }

    public List<Device> selectDeviceById(Integer lineID,Integer stationID,Integer type){
        return deviceMapper.selectDeviceById(lineID,stationID,type);
    }

    public List<Device> selectAllDevice(Integer stationID){
        return deviceMapper.selectAllDevice(stationID);
    }

    public Integer selectDeviceByName(Integer lineID,Integer stationID,String deviceID,String name,Integer id){
        return deviceMapper.selectDeviceByName(lineID,stationID,deviceID,name,id);
    }

    public List<Device> getDeviceList(Integer lineID,Integer stationID,Integer id){
        return deviceMapper.getDeviceList(lineID,stationID,id);
    }

    public PageUtil selectDevicePaging(Integer lineID,Integer stationID,Integer type,Integer id,
                                       Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(deviceMapper.count(lineID,stationID,type,id));
        pageUtil.setPageData(deviceMapper.selectDevicePaging(lineID,stationID,type,id,pageNum,pageSize));
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

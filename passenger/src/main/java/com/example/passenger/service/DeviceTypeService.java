package com.example.passenger.service;

import com.example.passenger.entity.DeviceType;
import com.example.passenger.mapper.DeviceTypeMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceTypeService.class);

    @Autowired
    DeviceTypeMapper deviceTypeMapper;

    public List<DeviceType> selectAllDeviceType(){
        return deviceTypeMapper.selectAllDeviceType();
    }

    public DeviceType selectDeviceTypeById(Integer id){
        return deviceTypeMapper.selectDeviceTypeById(id);
    }

    public PageUtil selectDeviceTypePaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(deviceTypeMapper.count());
        pageUtil.setPageData(deviceTypeMapper.selectDeviceTypePaging(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDeviceType(DeviceType device_type){
        try {
            return deviceTypeMapper.addDeviceType(device_type);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDeviceType(DeviceType device_type){
        try {
            return deviceTypeMapper.updateDeviceType(device_type);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDeviceType(Integer id){
        try {
            return deviceTypeMapper.deleteDeviceType(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

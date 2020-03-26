package com.example.passenger.service;

import com.example.passenger.entity.DeviceSpot;
import com.example.passenger.mapper.DeviceSpotMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceSpotService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceSpotService.class);

    @Autowired
    DeviceSpotMapper deviceSpotMapper;

    public DeviceSpot selectDeviceSpotById(Integer id) {
        return deviceSpotMapper.selectDeviceSpotById(id);
    }

    public DeviceSpot selectDeviceSpotByCtrlType(Integer id, Integer type, Integer ctrlType) {
        return deviceSpotMapper.selectDeviceSpotByCtrlType(id, type, ctrlType);
    }

    public List<DeviceSpot> selectAllDeviceSpot() {
        return deviceSpotMapper.selectAllDeviceSpot();
    }

    public List<DeviceSpot> selectDeviceSpot(Integer type) {
        return deviceSpotMapper.selectDeviceSpot(type);
    }

    public Integer selectDeviceSpotByName(Integer deviceType, String name, Integer id) {
        return deviceSpotMapper.selectDeviceSpotByName(deviceType, name, id);
    }

    public PageUtil selectDeviceSpotPaging(Integer deviceType, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(deviceSpotMapper.count(deviceType));
        pageUtil.setPageData(deviceSpotMapper.selectDeviceSpotPaging(deviceType, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDeviceSpot(DeviceSpot deviceSpot) {
        try {
            return deviceSpotMapper.addDeviceSpot(deviceSpot);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDeviceSpot(DeviceSpot deviceSpot) {
        try {
            return deviceSpotMapper.updateDeviceSpot(deviceSpot);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }


    @Transactional
    public Integer deleteDeviceSpot(Integer id) {
        try {
            return deviceSpotMapper.deleteDeviceSpot(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

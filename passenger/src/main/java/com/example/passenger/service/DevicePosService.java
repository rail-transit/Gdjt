package com.example.passenger.service;

import com.example.passenger.entity.DevicePos;
import com.example.passenger.mapper.DevicePosMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevicePosService {
    private static final Logger logger = LoggerFactory.getLogger(DevicePosService.class);

    @Autowired
    DevicePosMapper devicePosMapper;

    public List<DevicePos> selectAllDevicePos() {
        return devicePosMapper.selectAllDevicePos();
    }

    public Integer selectDevicePosByName(String name, Integer id) {
        return devicePosMapper.selectDevicePosByName(name, id);
    }

    public DevicePos selectDevicePosById(Integer id) {
        return devicePosMapper.selectDevicePosById(id);
    }

    public PageUtil selectDevicePosPaging(Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devicePosMapper.count());
        pageUtil.setPageData(devicePosMapper.selectDevicePosPaging(pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevicePos(DevicePos device_pos) {
        try {
            device_pos.setIsEdit(1);
            return devicePosMapper.addDevicePos(device_pos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDevicePos(DevicePos device_pos) {
        try {
            return devicePosMapper.updateDevicePos(device_pos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDevicePos(Integer id) {
        try {
            return devicePosMapper.deleteDevicePos(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

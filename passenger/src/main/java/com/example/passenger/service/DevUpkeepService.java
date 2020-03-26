package com.example.passenger.service;

import com.example.passenger.entity.DevUpkeep;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.vo.DevUpkeepVo;
import com.example.passenger.mapper.DevUpkeepMapper;
import com.example.passenger.mapper.DeviceMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DevUpkeepService {
    private static final Logger logger = LoggerFactory.getLogger(DevUpkeepService.class);

    @Autowired
    DevUpkeepMapper devUpkeepMapper;
    @Autowired
    DeviceMapper deviceMapper;

    public List<DevUpkeep> selectDevUpkeepByEndTimeIsNull(Integer deviceID) {
        return devUpkeepMapper.selectDevUpkeepByEndTimeIsNull(deviceID);
    }

    public List<Map<String, String>> getDevUpKeep(DevUpkeepVo devUpkeep) {
        return devUpkeepMapper.getDevUpKeep(devUpkeep);
    }

    public PageUtil selectDevUpkeepByEndTimeNotNull(Integer deviceID, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devUpkeepMapper.count(deviceID));
        pageUtil.setPageData(devUpkeepMapper.selectDevUpkeepByEndTimeNotNull(deviceID, pageNum, pageSize));
        return pageUtil;
    }

    public PageUtil maintainPaging(DevUpkeepVo devUpkeep, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devUpkeepMapper.maintainCount(devUpkeep));
        pageUtil.setPageData(devUpkeepMapper.maintainPaging(devUpkeep, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevUpkeep(DevUpkeep devUpkeep) {
        try {
            Device device = deviceMapper.selectDevice(devUpkeep.getDeviceID());
            devUpkeep.setDeviceID(device.getId());
            devUpkeep.setStationID(device.getStationID());
            devUpkeep.setLineID(device.getLineID());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            devUpkeep.setStartTime(sdf.format(new Date()));
            return devUpkeepMapper.addDevUpkeep(devUpkeep);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDevUpkeep(DevUpkeep devUpkeep) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            devUpkeep.setEndTime(sdf.format(new Date()));
            return devUpkeepMapper.updateDevUpkeep(devUpkeep);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

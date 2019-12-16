package com.example.passenger.service;

import com.example.passenger.entity.DevRepair;
import com.example.passenger.entity.Device;
import com.example.passenger.entity.vo.DevRepairVo;
import com.example.passenger.mapper.DevRepairMapper;
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
public class DevRepairService {
    private static final Logger logger = LoggerFactory.getLogger(DevRepairService.class);

    @Autowired
    DevRepairMapper devRepairMapper;
    @Autowired
    DeviceMapper deviceMapper;

    public List<DevRepair> selectDevRepairByEndTimeIsNull(Integer deviceID){
        return devRepairMapper.selectDevRepairByEndTimeIsNull(deviceID);
    }

    public List<Map<String,String>> getRepair(DevRepairVo devRepair){
        return devRepairMapper.getRepair(devRepair);
    }

    public PageUtil selectDevRepairByEndTimeNotNull(Integer deviceID,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devRepairMapper.count(deviceID));
        pageUtil.setPageData(devRepairMapper.selectDevRepairByEndTimeNotNull(deviceID,pageNum,pageSize));
        return pageUtil;
    }

    public PageUtil repairPaging(DevRepairVo devRepair,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devRepairMapper.repairCount(devRepair));
        pageUtil.setPageData(devRepairMapper.repairPaging(devRepair,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevRepair(DevRepair devRepair){
        try {
            Device device=deviceMapper.selectDevice(devRepair.getDeviceID());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            devRepair.setStartTime(sdf.format(new Date()));
            devRepair.setDeviceID(device.getId());
            devRepair.setStationID(device.getStationID());
            devRepair.setLineID(device.getLineID());
            return devRepairMapper.addDevRepair(devRepair);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDevRepair(DevRepair devRepair){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            devRepair.setEndTime(sdf.format(new Date()));
            return devRepairMapper.updateDevRepair(devRepair);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

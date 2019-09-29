package com.example.passenger.service;

import com.example.passenger.entity.DevRepair;
import com.example.passenger.mapper.DevRepairMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevRepairService {
    private static final Logger logger = LoggerFactory.getLogger(DevRepairService.class);

    @Autowired
    DevRepairMapper devRepairMapper;

    public List<DevRepair> selectDevRepairByEndTimeIsNull(){
        return devRepairMapper.selectDevRepairByEndTimeIsNull();
    }

    public PageUtil selectDevRepairByEndTimeNotNull(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devRepairMapper.count());
        pageUtil.setPageData(devRepairMapper.selectDevRepairByEndTimeNotNull(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevRepair(DevRepair devRepair){
        try {
            return devRepairMapper.addDevRepair(devRepair);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDevRepair(DevRepair devRepair){
        try {
            return devRepairMapper.updateDevRepair(devRepair);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

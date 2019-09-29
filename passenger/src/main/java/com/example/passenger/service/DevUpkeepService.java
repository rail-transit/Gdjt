package com.example.passenger.service;

import com.example.passenger.entity.DevUpkeep;
import com.example.passenger.mapper.DevUpkeepMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevUpkeepService {
    private static final Logger logger = LoggerFactory.getLogger(DevUpkeepService.class);

    @Autowired
    DevUpkeepMapper devUpkeepMapper;

    public List<DevUpkeep> selectDevUpkeepByEndTimeIsNull(){
       return devUpkeepMapper.selectDevUpkeepByEndTimeIsNull();
    }

    public PageUtil selectDevUpkeepByEndTimeNotNull(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(devUpkeepMapper.count());
        pageUtil.setPageData(devUpkeepMapper.selectDevUpkeepByEndTimeNotNull(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addDevUpkeep(DevUpkeep devUpkeep){
        try {
            return devUpkeepMapper.addDevUpkeep(devUpkeep);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateDevUpkeep(DevUpkeep devUpkeep){
        try {
            return devUpkeepMapper.updateDevUpkeep(devUpkeep);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

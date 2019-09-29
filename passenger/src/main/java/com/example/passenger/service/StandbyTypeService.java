package com.example.passenger.service;

import com.example.passenger.entity.StandbyType;
import com.example.passenger.mapper.StandbyTypeMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandbyTypeService {
    private static final Logger logger = LoggerFactory.getLogger(StandbyTypeService.class);

    @Autowired
    StandbyTypeMapper standbyTypeMapper;

    public PageUtil selectStandbyTypePaging(Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(standbyTypeMapper.count());
        pageUtil.setPageData(standbyTypeMapper.selectStandbyTypePaging(pageNum,pageSize));
        return pageUtil;
    }

    public List<StandbyType> selectAllStandbyType(){
        return standbyTypeMapper.selectAllStandbyType();
    }

    public Integer addStandbyType(StandbyType standbyType){
        try {
            return standbyTypeMapper.addStandbyType(standbyType);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer updateStandbyType(StandbyType standbyType){
        try {
            return standbyTypeMapper.updateStandbyType(standbyType);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer deleteStandbyType(Integer id){
        try {
            return standbyTypeMapper.deleteStandbyType(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

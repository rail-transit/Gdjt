package com.example.passenger.service;

import com.example.passenger.entity.LineStandby;
import com.example.passenger.entity.vo.LineStandbyVo;
import com.example.passenger.mapper.LineStandbyMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LineStandbyService {
    private static final Logger logger = LoggerFactory.getLogger(LineStandbyService.class);

    @Autowired
    LineStandbyMapper lineStandbyMapper;

    public List<Map<String,String>> getSparePart(Integer lineID){
        return lineStandbyMapper.getSparePart(lineID);
    }

    public PageUtil selectPaging(Integer lineID, Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(lineStandbyMapper.count(lineID));
        pageUtil.setPageData(lineStandbyMapper.selectPaging(lineID,pageNum,pageSize));
        return pageUtil;
    }

    public Integer selectLineStandbyExist(Integer lineID,Integer standbyType){
        return lineStandbyMapper.selectLineStandbyExist(lineID,standbyType);
    }

    public Integer getStandbyExistByType(Integer type){
        return lineStandbyMapper.getStandbyExistByType(type);
    }

    public List<LineStandbyVo> queryAllStandbyVo(){
        return lineStandbyMapper.queryAllStandbyVo();
    }

    @Transactional
    public Integer addLineStandby(LineStandby lineStandby){
        try {
            return lineStandbyMapper.addLineStandby(lineStandby);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateLineStandby(LineStandby lineStandby){
        try {
            return lineStandbyMapper.updateLineStandby(lineStandby);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteLineStandby(Integer id){
        try {
            return lineStandbyMapper.deleteLineStandby(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

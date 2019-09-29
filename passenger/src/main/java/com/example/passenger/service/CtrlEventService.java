package com.example.passenger.service;

import com.example.passenger.entity.CtrlEvent;
import com.example.passenger.mapper.CtrlEventMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CtrlEventService {
    private static final Logger logger = LoggerFactory.getLogger(CtrlEventService.class);

    @Autowired
    CtrlEventMapper ctrlEventMapper;

    public PageUtil selectAllCtrlEvent(Integer deviceID,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(ctrlEventMapper.count(deviceID));
        pageUtil.setPageData(ctrlEventMapper.selectAllCtrlEvent(deviceID,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer insertCtrlEvent(CtrlEvent ctrlEvent){
        try {
            return ctrlEventMapper.insertCtrlEvent(ctrlEvent);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

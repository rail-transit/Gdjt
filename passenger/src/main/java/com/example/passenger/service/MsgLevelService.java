package com.example.passenger.service;

import com.example.passenger.entity.MsgLevel;
import com.example.passenger.mapper.MsgLevelMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsgLevelService {
    private static final Logger logger = LoggerFactory.getLogger(MsgLevelService.class);

    @Autowired
    MsgLevelMapper msgLevelMapper;

    public MsgLevel selectMsgLevel(Integer id){
        return msgLevelMapper.selectMsgLevel(id);
    }

    public List<MsgLevel> selectMsgLevelAll(){
        return msgLevelMapper.selectMsgLevelAll();
    }

    public PageUtil selectPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(msgLevelMapper.count());
        pageUtil.setPageData(msgLevelMapper.selectPaging(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addMsgLevel(MsgLevel msgLevel){
        try {
            return msgLevelMapper.addMsgLevel(msgLevel);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMsgLevel(MsgLevel msgLevel){
        try{
            return msgLevelMapper.updateMsgLevel(msgLevel);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteMsgLevel(Integer id){
        try{
            return msgLevelMapper.deleteMsgLevel(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

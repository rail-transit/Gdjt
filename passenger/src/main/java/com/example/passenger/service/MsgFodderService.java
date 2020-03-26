package com.example.passenger.service;

import com.example.passenger.entity.MsgFodder;
import com.example.passenger.mapper.MsgFodderMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsgFodderService {
    private static final Logger logger = LoggerFactory.getLogger(MsgFodderService.class);

    @Autowired
    MsgFodderMapper msgFodderMapper;

    public MsgFodder selectMsgFodder(Integer id) {
        return msgFodderMapper.selectMsgFodder(id);
    }

    public List<MsgFodder> selectMsgFodderByType(Integer type) {
        return msgFodderMapper.selectMsgFodderByType(type);
    }

    public Integer selectMsgFodderByTitle(String title, Integer type, Integer id) {
        return msgFodderMapper.selectMsgFodderByTitle(title, type, id);
    }

    public PageUtil selectPaging(Integer type, Integer state, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(msgFodderMapper.count(type, state));
        pageUtil.setPageData(msgFodderMapper.selectPaging(type, state, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer updateState(Integer id, Integer state, String note) {
        try {
            return msgFodderMapper.updateState(id, state, note);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer addMsgFodder(MsgFodder msgFodder) {
        try {
            if (msgFodder.getContentEN() == null) {
                msgFodder.setContentEN("");
            }
            //设置初始状态为0
            msgFodder.setState(0);
            msgFodder.setNote("");
            return msgFodderMapper.addMsgFodder(msgFodder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMsgFodder(MsgFodder msgFodder) {
        try {
            if (msgFodder.getContentEN() == null) {
                msgFodder.setContentEN("");
            }
            return msgFodderMapper.updateMsgFodder(msgFodder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteMsgFodder(Integer id) {
        try {
            return msgFodderMapper.deleteMsgFodder(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

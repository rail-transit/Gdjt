package com.example.passenger.service;

import com.example.passenger.entity.PlayStyle;
import com.example.passenger.mapper.PlayListStyleMapper;
import com.example.passenger.mapper.PlayStyleMapper;
import com.example.passenger.mapper.StyleContentMapper;
import com.example.passenger.utils.MsgSend;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayStyleService {
    private static final Logger logger = LoggerFactory.getLogger(PlayStyleService.class);

    @Autowired
    PlayStyleMapper playStyleMapper;

    @Autowired
    PlayListStyleMapper playListStyleMapper;

    @Autowired
    StyleContentMapper styleContentMapper;

    @Autowired
    MsgSend msgSend;

    public PlayStyle selectPlayStyle(Integer id) {
        return playStyleMapper.selectPlayStyle(id);
    }

    public PageUtil selectPaging(Integer state, String name, Integer isTemplate, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playStyleMapper.count(state, name, isTemplate));
        pageUtil.setPageData(playStyleMapper.selectPaging(state, name, isTemplate, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addPlayStyle(PlayStyle playStyle) {
        try {
            return playStyleMapper.addPlayStyle(playStyle);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayStyleContent(Integer id, String content) {
        try {
            return playStyleMapper.updatePlayStyleContent(id, content);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayStyle(PlayStyle playStyle) {
        try {
            return playStyleMapper.updatePlayStyle(playStyle);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayStyle(PlayStyle playStyle) {
        try {
            Integer i = playStyleMapper.deletePlayStyle(playStyle);
            if (i > 0) {
                //styleContentMapper.deleteStyleContentByStyleID(format.getId());
                playListStyleMapper.deleteByStyleID(playStyle.getId());
            }
            return i;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

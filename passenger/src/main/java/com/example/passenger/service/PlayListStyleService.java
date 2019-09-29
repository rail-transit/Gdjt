package com.example.passenger.service;

import com.example.passenger.entity.PlayListStyle;
import com.example.passenger.mapper.PlayListStyleMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayListStyleService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListStyleService.class);

    @Autowired
    PlayListStyleMapper playListStyleMapper;

    public PlayListStyle selectPlayListStyle(Integer id){
        return playListStyleMapper.selectPlayListStyle(id);
    }

    public PageUtil selectPaging(Integer playListID,Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListStyleMapper.count(playListID));
        pageUtil.setPageData(playListStyleMapper.selectPaging(playListID,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addPlayListStyle(PlayListStyle playListStyle){
        try {
            return playListStyleMapper.addPlayListStyle(playListStyle);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updatePlayListStyle(PlayListStyle playListStyle){
        try {
            return playListStyleMapper.updatePlayListStyle(playListStyle);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deletePlayListStyle(Integer id){
        try {
            return playListStyleMapper.deletePlayListStyle(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

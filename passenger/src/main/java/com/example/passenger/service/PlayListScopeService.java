package com.example.passenger.service;

import com.example.passenger.entity.PlayListScope;
import com.example.passenger.mapper.PlayListScopeMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayListScopeService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListScopeService.class);

    @Autowired
    PlayListScopeMapper playListScopeMapper;

    public PlayListScope selectPlayListScope(Integer listID){
        return playListScopeMapper.selectPlayListScope(listID);
    }

    public PageUtil selectPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListScopeMapper.count());
        pageUtil.setPageData(playListScopeMapper.selectPaging(pageNum,pageSize));
        return pageUtil;
    }

    public Integer addPlayListScope(PlayListScope playListScope){
        try {
            return playListScopeMapper.addPlayListScope(playListScope);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer updatePlayListScope(PlayListScope playListScope){
        try {
            return playListScopeMapper.updatePlayListScope(playListScope);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer deletePlayListScope(Integer listID){
        try {
            return playListScopeMapper.deletePlayListScope(listID);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

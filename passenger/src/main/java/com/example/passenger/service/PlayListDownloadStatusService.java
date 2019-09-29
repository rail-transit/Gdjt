package com.example.passenger.service;

import com.example.passenger.mapper.PlayListDownloadStatusMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayListDownloadStatusService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListDownloadStatusService.class);

    @Autowired
    PlayListDownloadStatusMapper playListDownloadStatusMapper;

    public PageUtil selectPaging(Integer playListID,Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(playListDownloadStatusMapper.count(playListID));
        pageUtil.setPageData(playListDownloadStatusMapper.selectPaging(playListID,pageNum,pageSize));
        return pageUtil;
    }
}

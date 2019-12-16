package com.example.passenger.service;

import com.example.passenger.entity.PlayListDownloadStatus;
import com.example.passenger.mapper.PlayListDownloadStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayListDownloadStatusService {
    private static final Logger logger = LoggerFactory.getLogger(PlayListDownloadStatusService.class);

    @Autowired
    PlayListDownloadStatusMapper playListDownloadStatusMapper;

    public List<PlayListDownloadStatus> selectDownloadStatus(Integer playlistID,Integer clientID){
        return playListDownloadStatusMapper.selectDownloadStatus(playlistID,clientID);
    }


    @Transactional
    public Integer updateDownload(PlayListDownloadStatus playListDownloadStatus){
        try {
            return playListDownloadStatusMapper.updateDownload(playListDownloadStatus);
        }catch (Exception e){
            e.printStackTrace();
            return-1;
        }
    }

    public Integer deleteDownload(Integer id){
        try {
            return playListDownloadStatusMapper.deleteDownload(id);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

}

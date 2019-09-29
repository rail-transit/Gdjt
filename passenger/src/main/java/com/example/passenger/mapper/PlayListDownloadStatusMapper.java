package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListDownloadStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListDownloadStatusMapper {

    List<PlayListDownloadStatus> selectPaging(@Param("playListID") Integer playListID,
                                              @Param("pageNum") Integer pageNum,
                                              @Param("pageSize") Integer pageSize);
    Integer count(@Param("playListID") Integer playListID);

    Integer selectDownload(@Param("playlistID") String playlistID,@Param("clientID") String clientID,
                           @Param("fileName") String fileName);

    Integer addDownload(PlayListDownloadStatus playListDownloadStatus);

    Integer updateDownload(PlayListDownloadStatus playListDownloadStatus);
}

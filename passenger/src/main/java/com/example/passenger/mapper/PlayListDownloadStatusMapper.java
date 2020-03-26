package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListDownloadStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListDownloadStatusMapper {

    List<PlayListDownloadStatus> selectDownloadStatus(@Param("playlistID") Integer playlistID,
                                                      @Param("clientID") Integer clientID);

    Integer selectDownload(@Param("playlistID") String playlistID,@Param("clientID") String clientID,
                           @Param("fileName") String fileName);

    Integer addDownload(PlayListDownloadStatus playListDownloadStatus);

    Integer updateDownload(PlayListDownloadStatus playListDownloadStatus);

    Integer deleteDownload(Integer id);
}

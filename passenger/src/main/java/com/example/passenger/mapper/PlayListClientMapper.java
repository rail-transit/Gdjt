package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListClient;
import com.example.passenger.entity.vo.PlayListClientVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlayListClientMapper {

    PlayListClient selectPlayListClient(Integer id);

    List<PlayListClient> selectPaging(@Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer count();

    List<PlayListClientVo> getDownloadSpeed(@Param("playListID") Integer playListID,
                                            @Param("pageNum") Integer pageNum,
                                            @Param("pageSize") Integer pageSize);
    Integer DownloadSpeedCount(Integer playListID);

    List<PlayListClientVo> releasePaging(@Param("lineID") Integer lineID,
                                         @Param("stationID") Integer stationID,
                                         @Param("deviceID") Integer deviceID,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         @Param("pageNum") Integer pageNum,
                                         @Param("pageSize") Integer pageSize);

    Integer releaseCount(@Param("lineID") Integer lineID,
                         @Param("stationID") Integer stationID,
                         @Param("deviceID") Integer deviceID,
                         @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    List<Map<String,String>> getVideoRelease(@Param("lineID") Integer lineID,
                                             @Param("stationID") Integer stationID,
                                             @Param("deviceID") Integer deviceID,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    PlayListClient selectClientByPlayListID(@Param("playListID") Integer playListID,
                                            @Param("clientID") Integer clientID);

    List<PlayListClient> previousRelease(@Param("playListID") Integer playListID,
                                   @Param("clientID") Integer clientID,
                                   @Param("auditTime") String auditTime);

    List<PlayListClient> selectPlayListClientBySequence(Integer clientID);

    Integer addPlayListClient(PlayListClient playListClient);

    Integer updatePlayListClient(PlayListClient playListClient);

    Integer deletePlayListClient(Integer id);
}

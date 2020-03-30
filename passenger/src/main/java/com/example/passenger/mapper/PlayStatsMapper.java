package com.example.passenger.mapper;

import com.example.passenger.entity.PlayStats;
import com.example.passenger.entity.vo.PlayStatsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlayStatsMapper {

    List<Map<String,String>> getPlayStats(@Param("lineID") Integer lineID,
                                          @Param("stationID") Integer stationID,
                                          @Param("deviceID") Integer deviceID,
                                          @Param("type") Integer type,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);

    List<PlayStatsVo> playStatsPaging(@Param("lineID") Integer lineID,
                                      @Param("stationID") Integer stationID,
                                      @Param("deviceID") Integer deviceID,
                                      @Param("type") Integer type,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer playStatsCount(@Param("lineID") Integer lineID,
                           @Param("stationID") Integer stationID,
                           @Param("deviceID") Integer deviceID,
                           @Param("type") Integer type,
                           @Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    Integer addPlayStats(PlayStats playStats);
}

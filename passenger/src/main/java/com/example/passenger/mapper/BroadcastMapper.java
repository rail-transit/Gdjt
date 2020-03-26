package com.example.passenger.mapper;

import com.example.passenger.entity.Broadcast;
import com.example.passenger.entity.vo.BroadcastVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BroadcastMapper {

    List<Map<String,String>> getBroadcast(@Param("lineID") Integer lineID,
                                          @Param("stationID") Integer stationID,
                                          @Param("deviceID") Integer deviceID,
                                          @Param("type") Integer type,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);

    List<BroadcastVo> broadcastPaging(@Param("lineID") Integer lineID,
                                      @Param("stationID") Integer stationID,
                                      @Param("deviceID") Integer deviceID,
                                      @Param("type") Integer type,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer broadcastCount(@Param("lineID") Integer lineID,
                           @Param("stationID") Integer stationID,
                           @Param("deviceID") Integer deviceID,
                           @Param("type") Integer type,
                           @Param("startDate") String startDate,
                           @Param("endDate") String endDate);

    Integer addBroadcast(Broadcast broadcast);
}

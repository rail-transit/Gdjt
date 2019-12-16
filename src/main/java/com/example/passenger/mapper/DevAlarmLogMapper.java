package com.example.passenger.mapper;

import com.example.passenger.entity.DevAlarmLog;
import com.example.passenger.entity.vo.DevAlarmLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DevAlarmLogMapper {

    List<DevAlarmLogVo> warningPaging(@Param("alarmType") Integer alarmType,
                                      @Param("lineID") Integer lineID,
                                      @Param("stationID") Integer stationID,
                                      @Param("deviceID") Integer deviceID,
                                      @Param("type") Integer type,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer warningCount(@Param("alarmType") Integer alarmType,
                         @Param("lineID") Integer lineID,
                         @Param("stationID") Integer stationID,
                         @Param("deviceID") Integer deviceID,
                         @Param("type") Integer type,
                         @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    List<Map<String,String>> getWarning(@Param("alarmType") Integer alarmType,
                                        @Param("lineID") Integer lineID,
                                        @Param("stationID") Integer stationID,
                                        @Param("deviceID") Integer deviceID,
                                        @Param("type") Integer type,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

    Integer addDevAlarmLog(DevAlarmLog devAlarmLog);
}

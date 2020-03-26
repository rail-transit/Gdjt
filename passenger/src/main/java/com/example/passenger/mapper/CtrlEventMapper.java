package com.example.passenger.mapper;

import com.example.passenger.entity.CtrlEvent;
import com.example.passenger.entity.vo.CtrlEventVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CtrlEventMapper {

    List<CtrlEventVo> selectAllCtrlEvent(@Param("deviceID") Integer deviceID,
                                       @Param("pageNum")Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count(@Param("deviceID") Integer deviceID);

    String getEventTime();

    List<CtrlEventVo> controlPaging(@Param("lineID") Integer lineID,
                                    @Param("stationID") Integer stationID,
                                    @Param("deviceID") Integer deviceID,
                                    @Param("type") Integer type,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate,
                                    @Param("pageNum")Integer pageNum,
                                    @Param("pageSize") Integer pageSize);
    Integer controlCount(@Param("lineID") Integer lineID,
                         @Param("stationID") Integer stationID,
                         @Param("deviceID") Integer deviceID,
                         @Param("type") Integer type,
                         @Param("startDate") String startDate,
                         @Param("endDate") String endDate);

    List<Map<String,String>> getControl(@Param("lineID") Integer lineID,
                                        @Param("stationID") Integer stationID,
                                        @Param("deviceID") Integer deviceID,
                                        @Param("type") Integer type,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

    Integer insertCtrlEvent(CtrlEvent ctrlEvent);
}

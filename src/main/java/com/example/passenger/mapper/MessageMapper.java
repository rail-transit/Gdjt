package com.example.passenger.mapper;

import com.example.passenger.entity.Message;
import com.example.passenger.entity.vo.MessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MessageMapper {

    Message selectMessage(Integer id);

    List<MessageVo> selectPaging(@Param("state") Integer state,@Param("msg") String msg,
                               @Param("playState") Integer playState,@Param("pageNum") Integer pageNum,
                               @Param("pageSize") Integer pageSize);

    Integer count(@Param("state") Integer state,@Param("msg") String msg,
                  @Param("playState") Integer playState);

    List<MessageVo> messageStatistics(@Param("lineID") Integer lineID,
                                      @Param("stationID") Integer stationID,
                                      @Param("deviceID") Integer deviceID,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate,
                                      @Param("pageNum") Integer pageNum,
                                      @Param("pageSize") Integer pageSize);

    Integer countStatistics(@Param("lineID") Integer lineID,
                            @Param("stationID") Integer stationID,
                            @Param("deviceID") Integer deviceID,
                            @Param("startDate") String startDate,
                            @Param("endDate") String endDate);

    List<Map<String,String>> getRelease(@Param("lineID") Integer lineID,
                                        @Param("stationID") Integer stationID,
                                        @Param("deviceID") Integer deviceID,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

    Integer selectMessageID();

    List<Message> getMessageByIsPlanMsg();

    Integer getMessageByDeviceID(Integer deviceID);

    Integer getMaxLevel(Integer deviceID);

    Message getMessageByLevel(@Param("deviceID") Integer deviceID,
                              @Param("level")String level);

    List<Message> getMessage(@Param("level") String level,
                             @Param("deviceID") Integer deviceID);

    List<Message> selectMessageByPlanMsg();

    List<Message> selectMessageByStartTime();

    List<Message> selectMessageByEndTime();

    Integer addMessage(Message message);

    Integer updateMessage(@Param("id") Integer id,@Param("state") Integer state,
                          @Param("playState") Integer playState);

    Integer deleteMessage(Integer id);
}

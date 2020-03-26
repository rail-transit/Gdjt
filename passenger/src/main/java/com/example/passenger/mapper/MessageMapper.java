package com.example.passenger.mapper;

import com.example.passenger.entity.Message;
import com.example.passenger.entity.vo.MessageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MessageMapper {

    Message selectMessage(Integer id);

    List<MessageVo> selectPaging(@Param("lineID") Integer lineID,
                                 @Param("stationID") Integer stationID,
                                 @Param("deviceID") Integer deviceID,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate,
                                 @Param("state") Integer state,
                                 @Param("msg") String msg,
                                 @Param("playState") Integer playState,
                                 @Param("pageNum") Integer pageNum,
                                 @Param("pageSize") Integer pageSize);

    Integer count(@Param("lineID") Integer lineID,
                  @Param("stationID") Integer stationID,
                  @Param("deviceID") Integer deviceID,
                  @Param("startDate") String startDate,
                  @Param("endDate") String endDate,
                  @Param("state") Integer state,
                  @Param("msg") String msg,
                  @Param("playState") Integer playState);


    List<Map<String, String>> getRelease(@Param("lineID") Integer lineID,
                                         @Param("stationID") Integer stationID,
                                         @Param("deviceID") Integer deviceID,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate);

    Message getMessageByCondition(@Param("playState") Integer playState,
                                  @Param("deviceID") Integer deviceID,
                                  @Param("level") String level,
                                  @Param("msg") String msg);

    List<Message> getMessage(@Param("level") String level,
                             @Param("deviceID") Integer deviceID);

    Message getMessageByIsPlanMsg(Integer id);

    Integer getMaxLevel(Integer deviceID);

    List<Message> selectMessageByPlanMsg();

    List<Message> selectMessageByStartTime();

    List<Message> selectMessageByEndTime();

    List<Message> queryGroupingMessage();

    Integer addMessage(Message message);

    Integer updateMessage(@Param("id") Integer id,
                          @Param("state") Integer state,
                          @Param("playState") Integer playState);

    Integer deleteMessage(Integer id);
}

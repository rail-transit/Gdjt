package com.example.passenger.mapper;

import com.example.passenger.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {

    Message selectMessage(Integer id);

    List<Message> selectPaging(@Param("playState") Integer playState,@Param("pageNum") Integer pageNum,
                               @Param("pageSize") Integer pageSize);

    Integer count(@Param("playState") Integer playState);

    Integer selectMessageID();

    List<Message> selectMessageByStartTime();

    List<Message> selectMessageByEndTime();

    Integer addMessage(Message message);

    Integer updateMessage(@Param("id") Integer id,@Param("state") Integer state,
                          @Param("playState") Integer playState);

    Integer deleteMessage(Integer id);
}

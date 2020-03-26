package com.example.passenger.mapper;

import com.example.passenger.entity.MessageBoard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageBoardMapper {

    List<MessageBoard> messageBoardPaging(@Param("pageNum") Integer pageNum,
                                          @Param("pageSize") Integer pageSize);

    Integer count();

    Integer addMessageBoard(MessageBoard messageBoard);
}

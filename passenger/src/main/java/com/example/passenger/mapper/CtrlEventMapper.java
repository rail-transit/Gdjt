package com.example.passenger.mapper;

import com.example.passenger.entity.CtrlEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CtrlEventMapper {

    List<CtrlEvent> selectAllCtrlEvent(@Param("deviceID") Integer deviceID,
                                       @Param("pageNum")Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count(@Param("deviceID") Integer deviceID);

    Integer insertCtrlEvent(CtrlEvent ctrlEvent);
}

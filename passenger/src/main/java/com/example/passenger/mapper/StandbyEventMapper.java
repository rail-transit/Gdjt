package com.example.passenger.mapper;

import com.example.passenger.entity.StandbyEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StandbyEventMapper {

    List<StandbyEvent> selectAllStandbyEvent(@Param("lineID") Integer lineID);

    Integer addStandbyEvent(StandbyEvent standbyEvent);

    Integer updateStandbyEvent(StandbyEvent standbyEvent);

    Integer deleteStandbyEvent(Integer id);
}

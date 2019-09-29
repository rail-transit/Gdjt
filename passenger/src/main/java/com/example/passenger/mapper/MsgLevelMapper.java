package com.example.passenger.mapper;

import com.example.passenger.entity.MsgLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgLevelMapper {
    MsgLevel selectMsgLevel(Integer id);

    List<MsgLevel> selectPaging(@Param("pageNum") Integer pageNum, @Param("pageSize")Integer pageSize);

    List<MsgLevel> selectMsgLevelAll();

    Integer count();

    Integer addMsgLevel(MsgLevel msgLevel);

    Integer updateMsgLevel(MsgLevel msgLevel);

    Integer deleteMsgLevel(Integer id);
}

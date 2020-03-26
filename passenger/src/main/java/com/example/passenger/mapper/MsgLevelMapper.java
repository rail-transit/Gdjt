package com.example.passenger.mapper;

import com.example.passenger.entity.MsgLevel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgLevelMapper {
    MsgLevel selectMsgLevel(Integer id);

    MsgLevel selectMsgLevelByLevel(@Param("levelCode") String levelCode,
                                   @Param("level")Integer level);

    List<MsgLevel> selectMsgLevelAll();

    List<MsgLevel> selectMsgLevelByCode(String levelCode);

    List<MsgLevel> selectPaging(@Param("pageNum") Integer pageNum, @Param("pageSize")Integer pageSize);

    Integer count();

    Integer getMaxLevel();

    Integer selectLevelByID(@Param("level") Integer level,@Param("id") Integer id);

    Integer addMsgLevel(MsgLevel msgLevel);

    Integer updateMsgLevel(MsgLevel msgLevel);

    Integer deleteMsgLevel(Integer id);
}

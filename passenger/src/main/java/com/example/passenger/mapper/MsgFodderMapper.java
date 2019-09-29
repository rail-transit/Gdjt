package com.example.passenger.mapper;

import com.example.passenger.entity.MsgFodder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MsgFodderMapper {

    MsgFodder selectMsgFodder(Integer id);

    List<MsgFodder> selectPaging(@Param("type") Integer type,@Param("state") Integer state,
                                 @Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);
    Integer count(@Param("type") Integer type,@Param("state") Integer state);

    Integer updateState(@Param("id") Integer id,@Param("state") Integer state);

    Integer addMsgFodder(MsgFodder msgFodder);

    Integer updateMsgFodder(MsgFodder msgFodder);

    Integer deleteMsgFodder(Integer id);
}

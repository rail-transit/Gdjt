package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListStyle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListStyleMapper {

    PlayListStyle selectPlayListStyle(Integer playListID);

    List<PlayListStyle> getPlayListStyle(Integer playListID);

    PlayListStyle selectPlayListByStyleID(Integer styleID);

    List<PlayListStyle> selectPaging(@Param("playListID") Integer playListID,@Param("pageNum") Integer pageNum,
                                     @Param("pageSize") Integer pageSize);

    Integer updateTime(@Param("id") Integer id,
                       @Param("time") String time);

    Integer count(@Param("playListID") Integer playListID);

    Integer addPlayListStyle(PlayListStyle playListStyle);

    Integer updatePlayListStyle(PlayListStyle playListStyle);

    Integer deletePlayListStyle(Integer id);

    Integer deleteByStyleID(Integer styleID);

    Integer deleteByPlayListID(Integer playListID);
}

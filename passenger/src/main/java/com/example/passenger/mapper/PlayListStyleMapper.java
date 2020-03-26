package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListStyle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListStyleMapper {

    PlayListStyle selectPlayListStyle(Integer playListID);

    PlayListStyle selectPlayListByStyleID(Integer styleID);

    List<PlayListStyle> getPlayListStyle(Integer playListID);

    List<PlayListStyle> selectPaging(@Param("playListID") Integer playListID,
                                     @Param("pageNum") Integer pageNum,
                                     @Param("pageSize") Integer pageSize);

    Integer count(@Param("playListID") Integer playListID);

    Integer updateTime(@Param("id") Integer id,
                       @Param("time") String time);

    Integer addPlayListStyle(PlayListStyle playListStyle);

    Integer updatePlayListStyle(PlayListStyle playListStyle);

    Integer deletePlayListStyle(Integer id);

    Integer deleteByStyleID(Integer styleID);

    Integer deleteByPlayListID(Integer playListID);
}

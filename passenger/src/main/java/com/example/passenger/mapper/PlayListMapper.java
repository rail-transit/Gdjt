package com.example.passenger.mapper;

import com.example.passenger.entity.PlayList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListMapper {

    PlayList selectPlayList(Integer id);

    Integer selectPlayListID();

    List<PlayList> selectPaging(@Param("state") Integer state,@Param("timeLength") String timeLength,@Param("pageNum") Integer pageNum,
                                @Param("pageSize") Integer pageSize);

    Integer count(@Param("state") Integer state,@Param("timeLength") String timeLength);

    Integer auditPlayList(PlayList playList);

    Integer addPlayList(PlayList playList);

    Integer updatePlayList(PlayList playList);

    Integer deletePlayList(Integer id);
}

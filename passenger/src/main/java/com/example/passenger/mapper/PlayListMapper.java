package com.example.passenger.mapper;

import com.example.passenger.entity.PlayList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListMapper {

    PlayList selectPlayList(Integer id);

    Integer selectPlayListID();

    Integer selectCountByName(@Param("name") String name,
                              @Param("type") Integer type,
                              @Param("id") Integer id);

    List<PlayList> selectPaging(@Param("state") Integer state,@Param("type") Integer type,@Param("pageNum") Integer pageNum,
                                @Param("pageSize") Integer pageSize);

    Integer count(@Param("state") Integer state,@Param("type") Integer type);

    Integer updatePlayListByID(@Param("id") Integer id,
                               @Param("state") Integer state,
                               @Param("endDate")String endDate);

    Integer auditPlayList(PlayList playList);

    Integer addPlayList(PlayList playList);

    Integer updatePlayList(PlayList playList);

    Integer deletePlayList(PlayList playList);
}

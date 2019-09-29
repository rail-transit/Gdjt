package com.example.passenger.mapper;

import com.example.passenger.entity.PlayListClient;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayListClientMapper {

    PlayListClient selectPlayListClient(Integer id);

    List<PlayListClient> selectPaging(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count();

    Integer selectClientByPlayListID(@Param("playListID") Integer playListID,@Param("clientID") Integer clientID);

    List<PlayListClient> selectPlayListClientBySequence(Integer clientID);

    Integer selectClientByPlayID(@Param("clientID") Integer clientID,@Param("playID") Integer playID);

    Integer addPlayListClient(PlayListClient playListClient);

    Integer updatePlayListClient(PlayListClient playListClient);

    Integer deletePlayListClient(Integer id);
}

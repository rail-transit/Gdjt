package com.example.passenger.mapper;

import com.example.passenger.entity.PlayStyle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlayStyleMapper {

    PlayStyle selectPlayStyle(Integer id);

    Integer selectPlayStyleID();

    List<PlayStyle> selectPaging(@Param("state") Integer state,
                                 @Param("name")String name,
                                 @Param("isTemplate")Integer isTemplate,
                                 @Param("pageNum") Integer pageNum,
                                 @Param("pageSize") Integer pageSize);

    Integer count(@Param("state") Integer state,
                  @Param("name")String name,
                  @Param("isTemplate")Integer isTemplate);

    Integer updatePlayStyleContent(@Param("id") Integer id,
                                   @Param("content") String content);

    Integer addPlayStyle(PlayStyle playStyle);

    Integer updatePlayStyle(PlayStyle playStyle);

    Integer deletePlayStyle(PlayStyle playStyle);

    Integer comAddPlayStyle(@Param("sql") String sql);

    List<Map<String,String>> selectContent(@Param("sql") String sql);

}

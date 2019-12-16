package com.example.passenger.mapper;

import com.example.passenger.entity.Fodder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FodderMapper {

    List<Fodder> selectAllFodder(@Param("pageNum")Integer pageNum,
                                 @Param("pageSize")Integer pageSize);

    List<Fodder> selectAllFodderMaterial(@Param("type") Integer type,
                                         @Param("state")Integer state,
                                         @Param("name")String name,
                                         @Param("pageNum")Integer pageNum,
                                         @Param("pageSize")Integer pageSize);

    Integer selectAllFodderMaterialCount(@Param("type") Integer type,
                                         @Param("state")Integer state,
                                         @Param("name")String name);

    Integer count();

    List<Fodder> selectFodderByType(Integer type);

    Fodder selectFodderByID(Integer id);

    Integer selectFodderByName(@Param("name") String name,
                               @Param("id") Integer id,
                               @Param("type") Integer type);

    Integer addFodder(Fodder fodder);

    Integer updateFodder(Fodder fodder);

    Integer updateFodderState(Fodder fodder);

    Integer deleteFodder(Fodder fodder);
}

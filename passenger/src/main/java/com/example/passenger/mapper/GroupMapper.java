package com.example.passenger.mapper;

import com.example.passenger.entity.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {

    List<Group> selectAllGroup();

    Group selectGroupByID(Integer id);

    List<Group> selectGroupPaging(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

    Integer count();

    Integer addGroup(Group group);

    Integer updateGroup(Group group);

    Integer deleteGroup(Integer id);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.Rights;

import java.util.List;

public interface RightsMapper {
    //获取权限
    List<Rights> selectFirstRight(Integer parentID);

    Rights selectRightsByID(Integer id);
}

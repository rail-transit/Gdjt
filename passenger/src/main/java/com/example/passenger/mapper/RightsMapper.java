package com.example.passenger.mapper;

import com.example.passenger.entity.Rights;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RightsMapper {
    //获取权限
    List<Rights> selectFirstRight(Integer parentID);

    Rights selectRightsByID(Integer id);
}

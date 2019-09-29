package com.example.passenger.mapper;

import com.example.passenger.entity.DevUpkeep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevUpkeepMapper {
    List<DevUpkeep> selectDevUpkeepByEndTimeIsNull();

    List<DevUpkeep> selectDevUpkeepByEndTimeNotNull(@Param("pageNum") Integer pageNum, @Param("pageSize")Integer pageSize);

    Integer count();

    Integer addDevUpkeep(DevUpkeep devUpkeep);

    Integer updateDevUpkeep(DevUpkeep devUpkeep);
}

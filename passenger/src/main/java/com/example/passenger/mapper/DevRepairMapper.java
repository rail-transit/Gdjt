package com.example.passenger.mapper;

import com.example.passenger.entity.DevRepair;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevRepairMapper {
    List<DevRepair> selectDevRepairByEndTimeIsNull();

    List<DevRepair> selectDevRepairByEndTimeNotNull(@Param("pageNum") Integer pageNum, @Param("pageSize")Integer pageSize);

    Integer count();

    Integer addDevRepair(DevRepair devRepair);

    Integer updateDevRepair(DevRepair devRepair);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.DevRepair;
import com.example.passenger.entity.vo.DevRepairVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DevRepairMapper {
    List<DevRepair> selectDevRepairByEndTimeIsNull(Integer deviceID);

    List<DevRepair> selectDevRepairByEndTimeNotNull(@Param("deviceID") Integer deviceID,
                                                    @Param("pageNum") Integer pageNum,
                                                    @Param("pageSize")Integer pageSize);

    Integer count(Integer deviceID);

    List<DevRepairVo> repairPaging(@Param("devRepair") DevRepairVo devRepair,
                                   @Param("pageNum") Integer pageNum,
                                   @Param("pageSize")Integer pageSize);

    List<Map<String,String>> getRepair(DevRepairVo devRepair);

    Integer repairCount(DevRepairVo devRepair);

    Integer addDevRepair(DevRepair devRepair);

    Integer updateDevRepair(DevRepair devRepair);
}

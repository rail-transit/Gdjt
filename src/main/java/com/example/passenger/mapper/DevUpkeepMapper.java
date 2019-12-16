package com.example.passenger.mapper;

import com.example.passenger.entity.DevUpkeep;
import com.example.passenger.entity.vo.DevUpkeepVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DevUpkeepMapper {
    List<DevUpkeep> selectDevUpkeepByEndTimeIsNull(Integer deviceID);

    List<DevUpkeep> selectDevUpkeepByEndTimeNotNull(@Param("deviceID") Integer deviceID,
                                                    @Param("pageNum") Integer pageNum,
                                                    @Param("pageSize")Integer pageSize);
    Integer count(Integer deviceID);

    List<DevUpkeepVo> maintainPaging(@Param("devUpkeep") DevUpkeepVo devUpkeep,
                                     @Param("pageNum") Integer pageNum,
                                     @Param("pageSize") Integer pageSize);

    List<Map<String,String>> getDevUpKeep(DevUpkeepVo devUpkeep);

    Integer maintainCount(DevUpkeepVo devUpkeep);

    Integer addDevUpkeep(DevUpkeep devUpkeep);

    Integer updateDevUpkeep(DevUpkeep devUpkeep);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OperationLogMapper {

    List<OperationLog> selectPaging(@Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("type") String type,
                                    @Param("pageNum") Integer pageNum,
                                    @Param("pageSize") Integer pageSize);

    Integer count(@Param("startTime") String startTime,
                  @Param("endTime") String endTime,
                  @Param("type") String type);

    List<Map<String,String>> getJournal(@Param("startTime") String startTime,
                                        @Param("endTime") String endTime,
                                        @Param("type") String type);

    Integer addOperationLog(OperationLog operationLog);
}

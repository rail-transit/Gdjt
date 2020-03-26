package com.example.passenger.mapper;

import com.example.passenger.entity.LineStandby;
import com.example.passenger.entity.vo.LineStandbyVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LineStandbyMapper {
    List<LineStandbyVo> selectPaging(@Param("lineID") Integer lineID,
                                          @Param("pageNum") Integer pageNum,
                                          @Param("pageSize") Integer pageSize);
    Integer count(@Param("lineID") Integer lineID);

    List<Map<String,String>> getSparePart(Integer lineID);

    List<LineStandbyVo> queryAllStandbyVo();

    Integer selectLineStandbyExist(@Param("lineID") Integer lineID,
                                   @Param("standbyType") Integer standbyType);
    Integer getStandbyExistByType(Integer type);

    Integer addLineStandby(LineStandby lineStandby);

    Integer updateLineStandby(LineStandby lineStandby);

    Integer deleteLineStandby(Integer id);
}

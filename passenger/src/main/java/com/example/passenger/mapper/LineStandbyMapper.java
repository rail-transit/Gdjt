package com.example.passenger.mapper;

import com.example.passenger.entity.LineStandby;
import com.example.passenger.entity.vo.LineStandbyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LineStandbyMapper {
    List<LineStandbyVo> selectAllLineStandby(@Param("lineID") Integer lineID);

    Integer selectLineStandbyExist(@Param("lineID") Integer lineID,@Param("standbyType") Integer standbyType);

    Integer addLineStandby(LineStandby lineStandby);

    Integer updateLineStandby(LineStandby lineStandby);

    Integer deleteLineStandby(Integer id);
}

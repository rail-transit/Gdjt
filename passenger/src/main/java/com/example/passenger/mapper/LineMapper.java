package com.example.passenger.mapper;

import com.example.passenger.entity.Line;

import java.util.List;

public interface LineMapper {
    /**
     * 查询线路
     * @param id
     * @return
     */
    Line selectLine(Integer id);

    /**
     * 查询集合
     * @return
     */
    List<Line> selectAllLine();

    /**
     * 添加线路
     * @param line
     * @return
     */
    Integer addLine(Line line);

    /**
     * 修改线路
     * @param line
     * @return
     */
    Integer updateLine(Line line);

    /**
     * 删除线路
     * @param id
     * @return
     */
    Integer deleteLine(Integer id);
}

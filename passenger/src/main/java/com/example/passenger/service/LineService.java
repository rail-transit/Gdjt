package com.example.passenger.service;

import com.example.passenger.entity.Line;
import com.example.passenger.mapper.LineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LineService {
    private static final Logger logger = LoggerFactory.getLogger(LineService.class);

    @Autowired
    LineMapper lineMapper;

    public Line selectLine(Integer id) {
        return lineMapper.selectLine(id);
    }

    public List<Line> selectAllLine() {
        return lineMapper.selectAllLine();
    }

    public Integer selectLineByName(String lineID, String name, Integer id) {
        return lineMapper.selectLineByName(lineID, name, id);
    }

    @Transactional
    public Integer addLine(Line line) {
        try {
            return lineMapper.addLine(line);
        } catch (Exception e) {
            logger.error("添加线路异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer updateLine(Line line) {
        try {
            return lineMapper.updateLine(line);
        } catch (Exception e) {
            logger.error("修改线路异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteLine(Integer id) {
        try {
            return lineMapper.deleteLine(id);
        } catch (Exception e) {
            logger.error("删除线路异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}

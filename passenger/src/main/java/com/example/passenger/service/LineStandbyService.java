package com.example.passenger.service;

import com.example.passenger.entity.LineStandby;
import com.example.passenger.entity.vo.LineStandbyVo;
import com.example.passenger.mapper.LineStandbyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineStandbyService {
    private static final Logger logger = LoggerFactory.getLogger(LineStandbyService.class);

    @Autowired
    LineStandbyMapper lineStandbyMapper;

    public List<LineStandbyVo> selectAllLineStandby(Integer lineID){
        return lineStandbyMapper.selectAllLineStandby(lineID);
    }

    public Integer selectLineStandbyExist(Integer lineID,Integer standbyType){
        return lineStandbyMapper.selectLineStandbyExist(lineID,standbyType);
    }

    public Integer addLineStandby(LineStandby lineStandby){
        try {
            return lineStandbyMapper.addLineStandby(lineStandby);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer updateLineStandby(LineStandby lineStandby){
        try {
            return lineStandbyMapper.updateLineStandby(lineStandby);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer deleteLineStandby(Integer id){
        try {
            return lineStandbyMapper.deleteLineStandby(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

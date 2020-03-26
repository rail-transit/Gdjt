package com.example.passenger.service;

import com.example.passenger.entity.StandbyEvent;
import com.example.passenger.mapper.StandbyEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandbyEventService {
    private static final Logger logger = LoggerFactory.getLogger(StandbyEventService.class);

    @Autowired
    StandbyEventMapper standbyEventMapper;

    public List<StandbyEvent> selectAllStandbyEvent(Integer lineID) {
        return standbyEventMapper.selectAllStandbyEvent(lineID);
    }

    public Integer addStandbyEvent(StandbyEvent standbyEvent) {
        try {
            return standbyEventMapper.addStandbyEvent(standbyEvent);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer updateStandbyEvent(StandbyEvent standbyEvent) {
        try {
            return standbyEventMapper.updateStandbyEvent(standbyEvent);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }

    public Integer deleteStandbyEvent(Integer id) {
        try {
            return standbyEventMapper.deleteStandbyEvent(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

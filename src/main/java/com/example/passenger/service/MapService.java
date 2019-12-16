package com.example.passenger.service;

import com.example.passenger.entity.MapEntity;
import com.example.passenger.mapper.MapEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MapService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MapEntityMapper mapEntityMapper;

    public MapEntity selectMapByID(String id){
        return mapEntityMapper.selectMapByID(id);
    }

    @Transactional
    public Integer addMap(String id,String path){
        try {
            return mapEntityMapper.addMap(id,path);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateMap(String id,String path){
        try {
            return mapEntityMapper.updateMap(id,path);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

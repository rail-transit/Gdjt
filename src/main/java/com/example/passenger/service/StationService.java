package com.example.passenger.service;

import com.example.passenger.entity.Station;
import com.example.passenger.mapper.StationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StationService {
    private static final Logger logger = LoggerFactory.getLogger(StationService.class);

    @Autowired
    StationMapper stationMapper;

    public Station selectStation(Integer id){
        return stationMapper.selectStation(id);
    }

    public Station selectTrain(Integer id){
        return stationMapper.selectTrain(id);
    }

    public List<Station> selectAllStation(Integer lineID){
        return stationMapper.selectAllStation(lineID);
    }

    public List<Station> selectAllTrain(Integer lineID){
        return stationMapper.selectAllTrain(lineID);
    }

    public List<Station> queryAllStation(){
        return stationMapper.queryAllStation();
    }

    public Integer selectStationByName(Integer lineID,String stationID,String name,Integer id){
        return stationMapper.selectStationByName(lineID,stationID,name,id);
    }

    @Transactional
    public  Integer addStation(Station station){
        try {
            if(station.getIsTrain()==null){
                station.setIsTrain(0);
            }
            if(station.getIsCenter()==null){
                station.setIsCenter(0);
            }
            return stationMapper.addStation(station);
        }catch (Exception e){
            logger.error("添加车站异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer updateStation(Station station){
        try {
            return stationMapper.updateStation(station);
        }catch (Exception e){
            logger.error("修改车站异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteStation(Integer id){
        try {
            return stationMapper.deleteStation(id);
        }catch (Exception e){
            logger.error("添加车站异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteStationByLineId(Integer lineID){
        try {
            return stationMapper.deleteStationByLineId(lineID);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

}

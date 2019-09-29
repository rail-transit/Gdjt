package com.example.passenger.mapper;

import com.example.passenger.entity.Station;

import java.util.List;

public interface StationMapper {
    Station selectStation(Integer id);

    List<Station> selectAllStation(Integer lineID);

    List<Station> queryAllStation();

    Integer addStation(Station station);

    Integer updateStation(Station station);

    Integer deleteStation(Integer id);

    Integer deleteStationByLineId(Integer lineID);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.FodderType;

import java.util.List;

public interface FodderTypeMapper {

    List<FodderType> selectAllFodderType();

    FodderType selectFodderTypeByName(String name);
}

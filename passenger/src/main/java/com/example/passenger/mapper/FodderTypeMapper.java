package com.example.passenger.mapper;

import com.example.passenger.entity.FodderType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FodderTypeMapper {

    List<FodderType> selectAllFodderType();

    FodderType selectFodderTypeByName(String name);
}

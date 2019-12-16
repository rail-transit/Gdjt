package com.example.passenger.service;

import com.example.passenger.entity.FodderType;
import com.example.passenger.mapper.FodderTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FodderTypeService {
    private static final Logger logger = LoggerFactory.getLogger(FodderTypeService.class);

    @Autowired
    FodderTypeMapper fodderTypeMapper;

    public List<FodderType> selectAllFodderType(){
        return fodderTypeMapper.selectAllFodderType();
    }

    public FodderType selectFodderTypeByName(String name){
        return fodderTypeMapper.selectFodderTypeByName(name);
    }
}

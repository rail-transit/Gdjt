package com.example.passenger.service;

import com.example.passenger.entity.Rights;
import com.example.passenger.mapper.RightsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RightsService {
    @Autowired
    RightsMapper rightsMapper;

    public List<Rights> selectFirstRight(Integer parentID){
        return rightsMapper.selectFirstRight(parentID);
    }

    public Rights selectRightsByID(Integer id){
        return rightsMapper.selectRightsByID(id);
    }
}

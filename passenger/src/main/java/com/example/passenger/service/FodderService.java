package com.example.passenger.service;

import com.example.passenger.entity.Fodder;
import com.example.passenger.mapper.FodderMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FodderService {
    private static final Logger logger = LoggerFactory.getLogger(FodderService.class);

    @Autowired
    FodderMapper fodderMapper;

    public PageUtil selectAllFodder(Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(fodderMapper.count());
        pageUtil.setPageData(fodderMapper.selectAllFodder(pageNum,pageSize));
        return pageUtil;
    }

 /*   public List<Fodder> selectAllFodderMaterial(Fodder fodder){
        return fodderMapper.selectAllFodderMaterial(fodder);
    }*/
    public List<Fodder> selectFodderByType(Integer type){
        return fodderMapper.selectFodderByType(type);
    }

    public Fodder selectFodderByID(Integer id){
        return fodderMapper.selectFodderByID(id);
    }

    public PageUtil selectAllFodderMaterial(Integer type,Integer state,String name,Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(fodderMapper.selectAllFodderMaterialCount(type,state,name));
        pageUtil.setPageData(fodderMapper.selectAllFodderMaterial(type,state,name,pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addFodder(Fodder fodder){
        try {
            return fodderMapper.addFodder(fodder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateFodder(Fodder fodder){
        try {
            return fodderMapper.updateFodder(fodder);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateFodderState(Integer id,Integer state){
        try {
            return fodderMapper.updateFodderState(id,state);
        }catch (Exception  e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteFodder(Integer id){
        try {
            return fodderMapper.deleteFodder(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

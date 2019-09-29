package com.example.passenger.service;

import com.example.passenger.entity.Group;
import com.example.passenger.mapper.GroupMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    GroupMapper groupMapper;

    public List<Group> selectAllGroup(){
        return groupMapper.selectAllGroup();
    }

    public Group selectGroupByID(Integer id){
        return groupMapper.selectGroupByID(id);
    }

    public PageUtil selectGroupPaging(Integer pageNum,Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(groupMapper.count());
        pageUtil.setPageData(groupMapper.selectGroupPaging(pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addGroup(Group group){
        try {
            return groupMapper.addGroup(group);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer updateGroup(Group group){
        try {
            return groupMapper.updateGroup(group);
        }catch (Exception  e){
            logger.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteGroup(Integer id){
        try {
            return groupMapper.deleteGroup(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

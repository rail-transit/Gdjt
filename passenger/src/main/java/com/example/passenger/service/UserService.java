package com.example.passenger.service;

import com.example.passenger.entity.Users;
import com.example.passenger.mapper.UserMapper;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jinbin
 * @date 2018-07-08 20:52
 */
@Service("UserService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserMapper userMapper;

    public Users findByUsername(Users user){
        return userMapper.findByUsername(user.getName());
    }

    public Users findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    public PageUtil selectAllUsersByName(String username, Integer pageNum, Integer pageSize){
        PageUtil pageUtil=new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(userMapper.count(username));
        pageUtil.setPageData(userMapper.selectAllUsersByName(username,pageNum,pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer addUser(Users users){
        try {
            return userMapper.addUser(users);
        }catch (Exception e){
            logger.error("注册异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteUser(Integer Id){
        try {
            return userMapper.deleteUser(Id);
        }catch (Exception e){
            logger.error("删除异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    @Transactional
    public Integer updateUser(Users users){
        try {
            return userMapper.updateUser(users);
        }catch (Exception e){
            logger.error("修改异常",e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    @Transactional
    public Integer deleteUserByGroupId(Integer id){
        try {
            return userMapper.deleteUserByGroupId(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
    }
}

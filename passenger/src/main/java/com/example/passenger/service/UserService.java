package com.example.passenger.service;

import com.example.passenger.entity.Users;
import com.example.passenger.mapper.UserMapper;
import com.example.passenger.utils.MD5;
import com.example.passenger.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jinbin
 * @date 2018-07-08 20:52
 */
@Service("UserService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    UserMapper userMapper;

    public Users findByUsername(Users user) {
        return userMapper.findByUsername(user.getName());
    }

    public Users findUserById(Integer userId) {
        return userMapper.findUserById(userId);
    }

    public Integer selectUserIsNotID(Integer id, String name) {
        return userMapper.selectUserIsNotID(id, name);
    }

    public PageUtil selectAllUsersByName(String username, Integer pageNum, Integer pageSize) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNum(pageNum);
        pageUtil.setPageSize(pageSize);
        pageUtil.setRowCount(userMapper.count(username));
        pageUtil.setPageData(userMapper.selectAllUsersByName(username, pageNum, pageSize));
        return pageUtil;
    }

    @Transactional
    public Integer updateUserState(Integer id, Integer state) {
        try {
            String UpdateDate = sdf.format(new Date());
            return userMapper.updateUserState(id, state, UpdateDate);
        } catch (Exception e) {
            logger.error("修改异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer addUser(Users users) {
        try {
            users.setUpdateDate(sdf.format(new Date()));
            users.setRightLevel(0);
            users.setState(1);
            return userMapper.addUser(users);
        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public Integer deleteUser(Integer Id) {
        try {
            return userMapper.deleteUser(Id);
        } catch (Exception e) {
            logger.error("删除异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }

    }

    @Transactional
    public Integer updateUser(Users users) {
        try {
            users.setUpdateDate(sdf.format(new Date()));
            if (users.getPwd() != null && users.getPwd() != "") {
                users.setPwd(MD5.MD5(users.getPwd()));
            }
            return userMapper.updateUser(users);
        } catch (Exception e) {
            logger.error("修改异常", e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    @Transactional
    public Integer deleteUserByGroupId(Integer id) {
        try {
            return userMapper.deleteUserByGroupId(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}

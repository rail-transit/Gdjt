package com.example.passenger.mapper;

import com.example.passenger.entity.Users;
import com.example.passenger.entity.vo.UsersVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户: Dao
 */
@Repository
public interface UserMapper {
    /**
     * 按用户名获取用户信息
     * @return
     */
    Users findByUsername(@Param("username") String username);

    /**
     * 按用户id获取用户信息
     * @param Id
     * @return
     */
    Users findUserById(@Param("Id") Integer Id);

    /**
     * 查询重复用户
     * @return
     */
    Integer selectUserIsNotID(@Param("id") Integer id,
                              @Param("name") String name);

    /**
     * 查询用户分页
     * @return
     */
    List<UsersVo> selectAllUsersByName(@Param("username") String username,
                                       @Param("pageNum") Integer pageNum,
                                       @Param("pageSize")Integer pageSize);

    /**
     * 查询用户总数
     * @return
     */
    Integer count(@Param("username") String username);

    /**
     * 修改用户状态
     * @return
     */
    Integer updateUserState(@Param("id") Integer id,
                            @Param("state") Integer state,
                            @Param("updateDate")String updateDate);

    /**
     * 添加用户信息
     * @param users
     * @return
     */
    Integer addUser(Users users);

    /**
     * 修改用户信息
     * @param users
     * @return
     */
    Integer updateUser(Users users);

    /**
     * 删除用户信息
     * @param Id
     * @return
     */
    Integer deleteUser(@Param("Id")Integer Id);

    /**
     * 按用户组id删除用户
     * @param groupID
     * @return
     */
    Integer deleteUserByGroupId(Integer groupID);
}

package com.example.passenger.mapper;

import com.example.passenger.entity.Users;
import com.example.passenger.entity.vo.UsersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户: Dao
 */
public interface UserMapper {
    /**
     * 按用户名获取用户信息
     * @return
     */
    Users findByUsername(@Param("username") String username);

    Integer selectUserIsNotID(@Param("id") Integer id,
                              @Param("name") String name);

    /**
     * 按用户id获取用户信息
     * @param Id
     * @return
     */
    Users findUserById(@Param("Id") String Id);

    /**
     * 查询所有用户分页
     * @return
     */
    List<UsersVo> selectAllUsersByName(@Param("username") String username,
                                       @Param("pageNum") Integer pageNum,
                                       @Param("pageSize")Integer pageSize);


    Integer count(@Param("username") String username);

    Integer updateUserState(@Param("id") Integer id,
                            @Param("state") Integer state);

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
     * 按用户id删除用户信息
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

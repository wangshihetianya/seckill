package com.zhl.sportplay.dao;

import com.zhl.sportplay.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User getUserByNameAndPwd(@Param("username") String userName, @Param("password") String password);

    List<String> getPermissionByRole(@Param("role") String role);

    List<User> getAllUser(@Param("username") String username, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);

    int getUserCounts(@Param("username") String username);

    int addUser(User user);

    int addUserRole(User user);

    int updateUser(User user);

    int updateUserRole(User user);

    int deleteUser(int id);

}

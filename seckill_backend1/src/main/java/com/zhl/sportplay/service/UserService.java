package com.zhl.sportplay.service;

import com.zhl.sportplay.pojo.MainMenu;
import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, Object> validateUser(User user);

    Map<String, Object> getUser(QueryInfo queryInfo);

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(int id);
}

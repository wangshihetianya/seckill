package com.zhl.sportplay.service;

import com.zhl.sportplay.dao.MenuMapper;
import com.zhl.sportplay.dao.UserMapper;
import com.zhl.sportplay.pojo.MainMenu;
import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.User;
import com.zhl.sportplay.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuMapper menuMapper;

    @Override
    public Map<String, Object> validateUser(User user) {

        //返回结果集
        HashMap<String, Object> resMap = new HashMap<>();

        String username = user.getUsername();
        String password = user.getPassword();

        User resultUser = userMapper.getUserByNameAndPwd(username, password);

        System.out.println("从数据库读出来的user:" + resultUser);
        //查询失败
        if (resultUser == null) {
            return null;
        }

        //查询成功
        //写回一个Token和对应角色
        String token = JwtUtil.createToken(resultUser.getId(), resultUser.getUsername(), resultUser.getRole());

        resMap.put("token", token);
        resMap.put("userid", resultUser.getId());
        resMap.put("role", resultUser.getRole());

        return resMap;

    }

    @Override
    public Map<String, Object> getUser(QueryInfo queryInfo) {

        String query = queryInfo.getQuery().isEmpty() ? null : "%" + queryInfo.getQuery() + "%";

        int userCounts = userMapper.getUserCounts("%" + queryInfo.getQuery() + "%");
        int pageStart = (queryInfo.getpageNum() - 1) * queryInfo.getPageSize();
        List<User> users = userMapper.getAllUser("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("userCounts", userCounts);
        resMap.put("userList", users);

        return resMap;
    }

    @Override
    public int addUser(User user) {
        //TODO:这里需要用事务

        int re1 = userMapper.addUser(user);
        int re2 = 0;
        if (re1 > 0) {
            //一般添加的用户都设置成普通用户
            user.setRole("user");
            re2 = userMapper.addUserRole(user);
        }
        return (re1 > 0 && re2 > 0) ? 1 : -1;
    }

    @Override
    public int updateUser(User user) {
        //TODO:这里需要用事务

        int re1 = userMapper.updateUser(user);
        int re2 = 0;
        if (re1 > 0) {
            re2 = userMapper.updateUserRole(user);
        }
        return (re1 > 0 && re2 > 0) ? 1 : -1;

    }

    @Override
    public int deleteUser(int id) {
        //不需要考虑角色
        //表的外键约束直接级联删除
        return userMapper.deleteUser(id);
    }
}

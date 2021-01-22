package com.zhl.sportplay.controller;

import com.alibaba.fastjson.JSON;
import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.User;
import com.zhl.sportplay.service.UserService;
import com.zhl.sportplay.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/user")
    public String getUserList(QueryInfo queryInfo) {

        Map<String, Object> resMap = userService.getUser(queryInfo);

        if (resMap == null) {
            resMap = new HashMap<>();
            resMap.put("flag", Response.FAIL);
        } else {
            resMap.put("flag", Response.SUCCESS);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/addUser")
    public String addUser(@RequestBody User user) {
        int i = userService.addUser(user);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/editUser")
    public String updateUser(@RequestBody User user) {

        int i = userService.updateUser(user);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/deleteUser")
    public String deleteUser(int id) {

        int i = userService.deleteUser(id);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

}

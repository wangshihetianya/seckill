package com.zhl.sportplay.controller;

import com.alibaba.fastjson.JSON;
import com.zhl.sportplay.pojo.User;
import com.zhl.sportplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login(@RequestBody User user) {
        String flag = "error";

        Map<String, Object> resMap = userService.validateUser(user);

        if (resMap == null) {
            resMap = new HashMap<>();
        } else {
            flag = "ok";
        }
        resMap.put("flag", flag);

        return JSON.toJSONString(resMap);
    }
}

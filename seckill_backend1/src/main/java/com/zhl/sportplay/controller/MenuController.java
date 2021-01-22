package com.zhl.sportplay.controller;

import com.alibaba.fastjson.JSON;
import com.zhl.sportplay.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String getMenu(@RequestParam String role) {
       String flag = "error";

        Map<String, Object> resMap = new HashMap<>();

        if (resMap != null) {
            flag = "ok";
        }
        resMap.put("flag", flag);
        resMap.put("menuList", menuService.getMenus(role));

        return JSON.toJSONString(resMap);
    }
}

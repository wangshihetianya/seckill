package com.zhl.sportplay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.SeckillActivity;
import com.zhl.sportplay.service.RedisService;
import com.zhl.sportplay.service.SeckillService;
import com.zhl.sportplay.util.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SeckillController {

    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/activity")
    public String getActivityList(QueryInfo queryInfo) {

        System.out.println(queryInfo);

        Map<String, Object> resMap = seckillService.getAllActivity(queryInfo);

        if (resMap == null) {
            resMap = new HashMap<>();
            resMap.put("flag", Response.FAIL);
        } else {
            resMap.put("flag", Response.SUCCESS);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/addActivity")
    public String addActivity(@RequestBody SeckillActivity seckillActivity) {

        System.out.println(seckillActivity);

        //TODO:这里要用事务吧?
        //操作mysql
        int i = seckillService.addActivity(seckillActivity);
        //操作redis
        redisService.addActivity(seckillActivity);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/editActivity")
    public String updateActivity(@RequestBody SeckillActivity seckillActivity) {

        System.out.println(seckillActivity);

        int i = seckillService.updateActivity(seckillActivity);
        redisService.updateActivity(seckillActivity);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/deleteActivity")
    public String deleteActivity(int id) {

        System.out.println(id);

        int i = seckillService.deleteActivity(id);
        redisService.deleteActivity(id);

        Map<String, Object> resMap = new HashMap<>();

        if (i > 0) {
            resMap.put("flag", Response.SUCCESS);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);

    }

    @RequestMapping("/seckill")
    public String getSeckillPath(@RequestBody SeckillActivity seckillActivity) {

        Map<String, Object> resMap = new HashMap<>();

        String realURL = seckillService.getRealSeckillURL(seckillActivity);

        if (realURL != null) {
            resMap.put("flag", Response.SUCCESS);
            resMap.put("realURL", realURL);
        } else {
            resMap.put("flag", Response.FAIL);
        }

        return JSON.toJSONString(resMap);
    }

    @PostMapping(value = "/realSeckill")
    public String realSeckill(@RequestBody String body) {

//        @RequestParam("activityID")Integer activityID, @RequestParam("userID")Integer userID

        JSONObject jsonObject = JSONObject.parseObject(body);
        int activityID = jsonObject.getInteger("activityID");
        int userID = jsonObject.getInteger("userID");
//
//
//        System.out.println(body);
//        System.out.println("activityID为：" + activityID);
//        System.out.println("userID为：" + userID);
//        return null;



        Map<String, Object> resMap = new HashMap<>();

        int res = seckillService.seckill(activityID, userID);

        switch (res) {
            case 0:
                resMap.put("flag", Response.FAIL);
                break;
            case 1:
                resMap.put("flag", Response.SUCCESS);
                resMap.put("isSeckillSuccess", 0);
                break;
            case 2:
                resMap.put("flag", Response.SUCCESS);
                resMap.put("isSeckillSuccess", 1);
                break;
            default:
                break;
        }

        return JSON.toJSONString(resMap);
    }

    @PostMapping(value = "/result")
    public String seckillResult(@RequestBody String body) {


        JSONObject jsonObject = JSONObject.parseObject(body);
        int activityID = jsonObject.getInteger("activityID");
        int userID = jsonObject.getInteger("userID");



        Map<String, Object> resMap = new HashMap<>();

        int res = seckillService.getResult(activityID, userID);

        switch (res) {
            case 0:
                resMap.put("flag", Response.FAIL);
                break;
            case 1:
                resMap.put("flag", Response.SUCCESS);
                resMap.put("isFinallySuccess", 0);
                break;
            case 2:
                resMap.put("flag", Response.SUCCESS);
                resMap.put("isFinallySuccess", 1);
                break;
            default:
                break;
        }

        return JSON.toJSONString(resMap);
    }





}

package com.zhl.sportplay.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhl.sportplay.dao.OrderMapper;
import com.zhl.sportplay.dao.SeckillMapper;
import com.zhl.sportplay.pojo.Order;
import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.SeckillActivity;
import com.zhl.sportplay.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    SeckillMapper seckillMapper;
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisService redisService;
    @Autowired
    MQService mqService;

    @Override
    public Map<String, Object> getAllActivity(QueryInfo queryInfo) {

//        String query = queryInfo.getQuery().isEmpty() ? null : "%" + queryInfo.getQuery() + "%";

        int activityCount = seckillMapper.getActivityCount("%" + queryInfo.getQuery() + "%");
        int pageStart = (queryInfo.getpageNum() - 1) * queryInfo.getPageSize();
        List<SeckillActivity> activities = seckillMapper.getAllActivity("%" + queryInfo.getQuery() + "%", pageStart, queryInfo.getPageSize());

        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("activityCount", activityCount);
        resMap.put("activityList", activities);

        return resMap;
    }


    @Override
    public int addActivity(SeckillActivity seckillActivity) {
        seckillActivity.setStatus("未开始");

        int resultflag = seckillMapper.addSeckillActivity(seckillActivity);

        List<SeckillActivity> allActivity = seckillMapper.getAllActivity(seckillActivity.getTitle(), 0, 1);
        seckillActivity.setId(allActivity.get(0).getId());

        return resultflag;
    }

    @Override
    public int updateActivity(SeckillActivity seckillActivity) {
        return seckillMapper.updateSeckillActivity(seckillActivity);
    }

    @Override
    public int deleteActivity(int id) {
        return seckillMapper.deleteSeckillActivity(id);
    }

    @Override
    public String getRealSeckillURL(SeckillActivity seckillActivity) {
        //TODO:这里的时间按理来说应该从数据库中取，这里省事就直接用前端传来的参数了
        Date startTime = seckillActivity.getStartTime();
        Date currentTime = new Date();

        System.out.println("抢购开始时间为：" + startTime + ", " + startTime.getTime());
        System.out.println("当前时间为：" + currentTime + ", " + currentTime.getTime());

        if (currentTime.getTime() > startTime.getTime()) {
            //如果到了秒杀时间，返回真实的url地址
            //TODO:这里应该返回一个MD5加密的字符串
            return "/realSeckill";
        }

        return null;
    }

    @Override
    public int seckill(int activityID, int userID) {

        int redisSeckillRes = redisService.seckill(activityID, userID);

        //出现问题直接返回
        if (redisSeckillRes < 2) {
            return redisSeckillRes;
        }

        //没问题的话把订单传入消息队列
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activityID", activityID);
        jsonObject.put("userID", userID);

        //TODO:把订单传入消息队列
        mqService.add(jsonObject.toJSONString());


        return 2;

    }

    /**
     *
     * @param activityID
     * @param userID
     * @return 0表示还没抢购或抢购失败 1表示排队中 2表示抢购成功
     */
    @Override
    public int getResult(int activityID, int userID) {

        Order order = redisService.getOrder(activityID, userID);

        //redis有订单信息，说明还在排队中
        if (order != null) {
            return 1;
        }

        //查数据库
        int orderExist = orderMapper.checkOrder(activityID, userID);
        //订单已入库
        if (orderExist != 0) {
            return 2;
        }


        return 0;
    }
}

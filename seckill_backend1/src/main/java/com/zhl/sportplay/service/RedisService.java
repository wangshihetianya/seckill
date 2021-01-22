package com.zhl.sportplay.service;

import com.zhl.sportplay.pojo.Order;
import com.zhl.sportplay.pojo.SeckillActivity;

public interface RedisService {

    void addActivity(SeckillActivity seckillActivity);

    void deleteActivity(int id);

    void updateActivity(SeckillActivity seckillActivity);

//    void velidateUserSeckillHistory(int userId, int activityId);

    int seckill(int activityID, int userID);

    Order getOrder(int activityID, int userID);


}

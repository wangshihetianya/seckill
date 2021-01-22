package com.zhl.sportplay.service;

import com.zhl.sportplay.pojo.QueryInfo;
import com.zhl.sportplay.pojo.SeckillActivity;
import com.zhl.sportplay.pojo.User;



import java.util.Map;

public interface SeckillService {

    Map<String, Object> getAllActivity(QueryInfo queryInfo);

    int addActivity(SeckillActivity seckillActivity);

    int updateActivity(SeckillActivity seckillActivity);

    int deleteActivity(int id);

    String getRealSeckillURL(SeckillActivity seckillActivity);

    int seckill(int activityID, int userID);

    int getResult(int activityID, int userID);
}

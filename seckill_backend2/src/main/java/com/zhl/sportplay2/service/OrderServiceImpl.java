package com.zhl.sportplay2.service;

import com.alibaba.fastjson.JSONObject;
import com.zhl.sportplay2.dao.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "myqueue")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    @RabbitHandler
    /**
     * 只有在整个函数完成之后才算完成处理消息（不抛出异常），不然rabbit中的消息还会在消息队列中
     */
    public void putInStorage(String message) {
        System.out.println("收到的消息为：" + message);

        JSONObject jsonObject = JSONObject.parseObject(message);
        int activityID = jsonObject.getInteger("activityID");
        int userID = jsonObject.getInteger("userID");

        //将订单写入mysql
        orderMapper.addOrder(activityID, userID);

        //清除redis里的订单信息
        String setKey = "order" + activityID;
        String setValue = String.valueOf(userID);

        redisTemplate.opsForSet().remove(setKey, setValue);


    }
}

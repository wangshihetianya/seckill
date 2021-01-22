package com.zhl.sportplay.service;

import com.zhl.sportplay.pojo.Order;
import com.zhl.sportplay.pojo.SeckillActivity;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void addActivity(SeckillActivity seckillActivity) {

        //添加秒杀活动的商品数目
        String activityKey = "seckill" + seckillActivity.getId();

        redisTemplate.opsForValue().setIfAbsent(activityKey, String.valueOf(seckillActivity.getNumber()));

        System.out.println(redisTemplate.opsForValue().get(activityKey));

    }

    @Override
    public void deleteActivity(int id) {

        String activityKey = "seckill" + id;
        String orderKey = "order" + id;

        System.out.println("redis删除的key为：" + activityKey);

        //删除秒杀活动
        redisTemplate.delete(activityKey);

        //如果还存在相应的订单表，一起删除
        redisTemplate.delete(orderKey);

    }

    @Override
    public void updateActivity(SeckillActivity seckillActivity) {
        //添加秒杀活动的商品数目
        String activityKey = "seckill" + seckillActivity.getId();

        redisTemplate.opsForValue().setIfPresent(activityKey, String.valueOf(seckillActivity.getNumber()));

        System.out.println(redisTemplate.opsForValue().get(activityKey));
    }

    /**
     *
     * @param activityID
     * @param userID
     * @return 0表示事务失败 1表示商品已卖完 2表示成功下单
     */
    @Override
    public int seckill(int activityID, int userID) {
        String activityKey = "seckill" + activityID;
        String orderKey = "order" + activityID;

        //乐观锁保证不会超卖
        redisTemplate.watch(activityKey);

        //获取剩余商品数
        int leftGoodsNum = Integer.parseInt(redisTemplate.opsForValue().get(activityKey));

        if (leftGoodsNum > 0) {

            //允许事务
            redisTemplate.setEnableTransactionSupport(true);

            //开启事务
            redisTemplate.multi();

            //事务中的具体操作
            //减库存
            redisTemplate.opsForValue().decrement(activityKey);
            //下单，以便防止用户多次访问
            redisTemplate.opsForSet().add(orderKey, String.valueOf(userID));

            //执行事务
            List<Object> execResults = redisTemplate.exec();

            if (execResults == null || execResults.isEmpty()) {
                System.out.println("redis事务执行失败");
                return 0;
            } else {
                System.out.println("redis事务执行成功");
                return 2;
            }


        }

        redisTemplate.unwatch();

        return 1;

    }

    @Override
    public Order getOrder(int activityID, int userID) {

        String orderKey = "order" + activityID;

        if (redisTemplate.opsForSet().isMember(orderKey, String.valueOf(userID))) {
            return new Order(activityID, userID);
        }

        return null;
    }

    //    @Override
//    public void velidateUserSeckillHistory(int userId, int activityId) {
//
//    }
}

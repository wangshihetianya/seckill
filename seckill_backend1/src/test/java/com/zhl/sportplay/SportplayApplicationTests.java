package com.zhl.sportplay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SportplayApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {

//        redisTemplate.opsForValue().set("firstkey", "hello world");
//        System.out.println(redisTemplate.opsForValue().get("key"));
        redisTemplate.delete("firstkey");

//        System.out.println(redisTemplate.opsForValue().get("firstkey"));

    }

}

package com.zhl.sportplay2.service;


import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQServiceImpl implements MQService {


    @Override
    public void processMessage(String message) {





    }
}

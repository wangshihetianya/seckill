package com.zhl.sportplay.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQServiceImpl implements MQService{

    public static final String QUEUENAME = "myqueue";

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @Override
    public void add(String jsonStr) {

        rabbitTemplate.convertAndSend(QUEUENAME, jsonStr);

        System.out.println("消息传输成功！");

    }
}

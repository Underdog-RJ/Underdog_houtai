package com.atguigu.eduorder.controller;

import com.atguigu.eduorder.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/eduorder/test")
public class Test {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("testSend")
    public void testSend() {
        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setOrderNo(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("test-order", "test", order, new CorrelationData(UUID.randomUUID().toString()));
        }
    }

}

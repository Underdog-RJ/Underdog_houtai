package com.atguigu.eduorder.listener;

import com.atguigu.commonutils.order.SeckillQuickOrderTo;
import com.atguigu.eduorder.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "order.seckill.order")
public class OrderSeckillListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(SeckillQuickOrderTo seckillQuickOrderTo, Channel channel, Message message){

        log.info("准备创建秒杀单的详情信息");
        orderService.createSeckillOrder(seckillQuickOrderTo);

    }
}

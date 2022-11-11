package com.atguigu.eduorder.controller;


import com.atguigu.eduorder.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@RabbitListener(queues = "test-order-queue",ackMode = "MANUAL")
public class tt {

    @RabbitHandler
    public void recieveMessage(Message message, Order order, Channel channel) throws IOException, TimeoutException {
        System.out.println(message.toString());
        // channel内递增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if ((deliveryTag & 1) == 1) {
                // 签收消息，非批量模式
                channel.basicAck(deliveryTag, false);
                System.out.println("接收到消息..." + deliveryTag);
            } else {
                /**
                 * long deliveryTag, boolean multiple, boolean requeue
                 * requeue =true返回服务器
                 * requeue =false丢弃
                 *
                 */
                channel.basicNack(deliveryTag, false, false);
                System.out.println("拒绝到消息..." + deliveryTag);
                /**
                 * long deliveryTag, boolean requeue
                 */
//                channel.basicReject();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            channel.close();
        }
        System.out.println(deliveryTag);
    }

}

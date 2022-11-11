package com.atguigu.eduorder.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyMQConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange("order-event-exchange",true,false);
    }


    @Bean
    public Queue orderSeckillOrderQueue(){
        return new Queue("order.seckill.order",true,false,false);
    }


    @Bean
    public Binding orderSeckillOrderQueueBinding(){
        return new Binding(
                "order.seckill.order",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                null);
    }

    /**
     * 定制RabbitTemplate
     * 1.服务收到消息就回调
     * 1.spring.rabbitmq.publisher-confirms=true
     * 2.设置确认回调
     * 2.消息正确抵达队列进行回调
     * # 开启发送端消息抵达队列的确认
     * spring.rabbitmq.publisher-returns=true
     * # 只要抵达队列，以异步发送有限回调我们这个returnconfirm
     * spring.rabbitmq.template.mandatory=true
     * 3.消费端确认（保证每个消息被正确消费，此时才可以broker删除这个消息）
     *
     *      1.默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
     *          问题：我们收到很多消息，自动回复给服务器ack，只有一个消息处理成功，宕机了，发生消息丢失
     *          所以我们改为手动确认机制。只要我们没有明确告诉MQ,消息被接受，没有ack消息一直是unacked状态，
     *          即使Comsumer宕机，消息不会丢失，会重新变为Ready，下一次新的Consumer链接进来就发个他
     *     2.如何签收
     *          签收：业务成功就因签收
     *          channel.basicAck(deliveryTag, false);
     *          拒签失败拒签
     *          channel.basicNack(deliveryTag, false, false);
     *           channel.basicReject();
     *     3.确认的配置
     *     @RabbitListener(queues = "test-order-queue",ackMode = "MANUAL")
     *     spring.rabbitmq.listener.direct.acknowledge-mode=manual
     *
     */
    @PostConstruct // RabbitMQConfig对象创建完成之后，执行这个方法
    public void initRabbitTemplate() {
        // 设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 当前消息的唯一关联数据（这个是消息的唯一id）
             * @param b 消息是否成功收到
             * @param s 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("confirm..correlationData[" + correlationData + "]===>ack[" + b + "]===>s[" + s + "]");
            }
        });
        // 设置消息抵达队列的确认回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定的队列，就出发这个失败回调
             * @param message 投递失败的消息相信信息
             * @param replyCode 回复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 当时这个消息发送给那个交换机
             * @param routingKey 当时这个消息用那个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("Fail Message" + message + "===replayCode" + replyCode + "--" + replyText + "---" + exchange + "--->" + routingKey);
            }
        });
    }



}

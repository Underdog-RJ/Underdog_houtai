package com.atguigu.eduservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyMQConfig {

    /**
     * rabbit 自动创建
     * @return
     */
    @Bean
    public Queue livingDelayQueue(){
        Map<String,Object> arguments=new HashMap<>();
        arguments.put("x-dead-letter-exchange","living-event-exchange");
        arguments.put("x-dead-letter-routing-key","living.release.living");
        arguments.put("x-message-ttl",2*60*60000);
        Queue queue = new Queue("living.delay.queue", true, false, false,arguments);
       return queue;
    }

    @Bean
    public Queue livingReleaseQueue(){
        Queue queue = new Queue("living.delay.living.queue", true, false, false);
        return queue;
    }

    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange("living-event-exchange",true,false);

    }

    @Bean
    public Binding livingCreateBinding(){
       return new Binding("living.delay.queue",
                    Binding.DestinationType.QUEUE,
               "living-event-exchange",
               "living.create.living",null
               );
    }

    @Bean
    public Binding livingReleaseBinding(){
        return new Binding("living.delay.living.queue",
                Binding.DestinationType.QUEUE,
                "living-event-exchange",
                "living.release.living",null
        );
    }

}

package com.atguigu.pierce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

public class DelayQueue {

    @Autowired
    StringRedisTemplate redisTemplate;

    private final static String DELAY_QUEUE_KEY = "delay_queue:";

    public void delay() {
        String msg = "jsonstr";
        redisTemplate.opsForZSet().add(DELAY_QUEUE_KEY, msg, System.currentTimeMillis() + 5000);
    }

    public void loop() {
        while (!Thread.interrupted()) {
            Set<String> range = redisTemplate.opsForZSet().range(DELAY_QUEUE_KEY, 0, System.currentTimeMillis());
            if (range.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String msg = range.iterator().next();
            Long flag = redisTemplate.opsForZSet().remove(DELAY_QUEUE_KEY, msg);
            if (flag > 0) {
                // 处理逻辑
            }
        }
    }

}

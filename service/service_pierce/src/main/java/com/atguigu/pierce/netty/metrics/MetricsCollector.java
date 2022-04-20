package com.atguigu.pierce.netty.metrics;

import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MetricsCollector {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private final String METRICS_PREFIX = "metrics:collect";


    public void incrementChannelNum(Integer port) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(METRICS_PREFIX);
        // 如果不包含当前key，则把当前的key添加进去,更新当前的channels通道个数
        String portStr = String.valueOf(port);
        if (!ops.hasKey(portStr)) {
            Metrics metrics = new Metrics();
            metrics.setPort(port);
            metrics.setChannels(1);
            String json = JSONObject.toJSONString(metrics);
            ops.put(portStr, json);
        } else {
            String jsonStr = (String) ops.get(portStr);
            Metrics metrics = JSONObject.parseObject(jsonStr, Metrics.class);
            metrics.setChannels(metrics.getChannels() + 1);
            String json = JSONObject.toJSONString(metrics);
            ops.put(portStr, json);
        }
    }


    public void decrementChannelNum(Integer port) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(METRICS_PREFIX);
        String portStr = String.valueOf(port);
        // 如果不包含当前key，则把当前的key添加进去,更新当前的channels通道个数
        if (!ops.hasKey(portStr)) {

        } else {
            String jsonStr = (String) ops.get(portStr);
            Metrics metrics = JSONObject.parseObject(jsonStr, Metrics.class);
            metrics.setChannels(metrics.getChannels() - 1 > 0 ? metrics.getChannels() - 1 : 0);
            String json = JSONObject.toJSONString(metrics);
            ops.put(portStr, json);
        }
    }

    public void incrementReadInfo(Integer port, int msgLength) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(METRICS_PREFIX);
        // 如果不包含当前key，则把当前的key添加进去,更新当前的channels通道个数
        String portStr = String.valueOf(port);
        if (!ops.hasKey(portStr)) {

        } else {
            String jsonStr = (String) ops.get(portStr);
            Metrics metrics = JSONObject.parseObject(jsonStr, Metrics.class);
            metrics.setReadBytes(metrics.getReadBytes() + msgLength);
            metrics.setReadMsgs(metrics.getReadMsgs() + 1);
            String json = JSONObject.toJSONString(metrics);
            ops.put(portStr, json);
        }
    }

    public void incrementWriteInfo(Integer port, int msgLength) {
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(METRICS_PREFIX);
        // 如果不包含当前key，则把当前的key添加进去,更新当前的channels通道个数
        String portStr = String.valueOf(port);
        if (!ops.hasKey(portStr)) {

        } else {
            String jsonStr = (String) ops.get(portStr);
            Metrics metrics = JSONObject.parseObject(jsonStr, Metrics.class);
            metrics.setWroteBytes(metrics.getWroteBytes() + msgLength);
            metrics.setWroteMsgs(metrics.getWroteMsgs() + 1);
            String json = JSONObject.toJSONString(metrics);
            ops.put(portStr, json);
        }
    }


}

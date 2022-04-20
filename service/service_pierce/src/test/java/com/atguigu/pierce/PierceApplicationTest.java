package com.atguigu.pierce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PierceApplicationTest {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    ThreadLocal<Map> lockers = new ThreadLocal<>();

    private boolean _lock(String key) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 加锁,setIfAbsent相当于setNx，添加uuid的目的是，当解锁确保是同一个事务。
        return redisTemplate.opsForValue().setIfAbsent(key, uuid, 5, TimeUnit.SECONDS);
    }

    private void _unlock(String key) {
        // 删除锁，
        redisTemplate.delete(key);
    }

    private Map<String, Integer> currentLockers() {
        Map<String, Integer> refs = lockers.get();
        if (refs != null) {
            return refs;
        }
        lockers.set(new HashMap());
        return lockers.get();
    }

    public boolean lock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer integer = refs.get(key);
        if (integer != null) {
            refs.put(key, integer + 1);
            return true;
        }
        boolean flag = _lock(key);
        if (!flag)
            return false;
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key) {
        Map<String, Integer> refs = currentLockers();
        Integer integer = refs.get(key);
        if (integer == null) {
            return false;
        }
        integer--;
        if (integer <= 0) {
            _unlock(key);
            refs.remove(key);
        } else {
            refs.put(key, integer);
        }

        return true;

    }
}

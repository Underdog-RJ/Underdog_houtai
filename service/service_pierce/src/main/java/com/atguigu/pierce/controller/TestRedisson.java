package com.atguigu.pierce.controller;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping
@RestController
public class TestRedisson {


    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @RequestMapping
    @GetMapping("/hello")
    public String hello() {
        // 1.获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");

        // 2.加锁
        lock.lock(); // 阻塞时加锁。默认加锁时间都是30s
        // 所得自动续期，如果业务超长，运行时间自动个锁续上新的30s的时间，不用担心业务时间长，锁自动过期被删掉
        // 加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s以后自动删除
        lock.lock(10, TimeUnit.SECONDS);
        try {
            System.out.println("枷锁成功，执行业务..." + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            // 3.解锁，即使解锁代码，没有运行，redisson也不会出现死锁
            System.out.println("释放锁" + Thread.currentThread().getId());
            lock.unlock();
        }

        return "hello";
    }

    // 保证一定读取最新数据，修改期间，写锁是一个排它锁，读锁是一个共享锁
    // 写锁没释放，读锁就必须等待
    // 读 + 读 相当于无锁，并发读，只会在redis中记录，所以当前的读锁，他们都会同时加锁成功
    // 写 + 读 等待写锁释放
    // 写 + 写 阻塞
    // 读 + 写 有读锁写也需要等待
    // 只要有写得存在读都不喜欢等待
    @GetMapping("write")
    public String writeValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = lock.writeLock();
        String s = "";
        try {
            // 1.改数据加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("read")
    public String readValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        // 加读锁
        RLock rLock = lock.readLock();
        rLock.lock();

        String writeValue;
        try {
            writeValue = redisTemplate.opsForValue().get("writeValue");
        } finally {
            rLock.unlock();
        }
        return writeValue;
    }

    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire();// 获取一个信号，获取一个值
        park.tryAcquire();
        return "ok";
    }

    @GetMapping("/go")
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();// 获取一个信号，释放一个值
        return "ok";
    }

    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();// 等待闭锁都完成
        return "关门了";
    }

    @GetMapping("/gogogo/{id}")
    public String go(@PathVariable("id") Long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();// 计数减1
        return id + "班的人都走了...";
    }


}

package com.atguigu.scheduled;


import com.atguigu.commonutils.TimeUtil;
import com.atguigu.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 秒杀商品的定时上架
 *    每天晚上3点：上架最近3天需要秒杀的商品
 */
@Slf4j
@Service
public class SeckillSkuScheduled {

  @Autowired
  SeckillService seckillService;

  @Autowired
  RedissonClient redissonClient;

  private final String UPLOAD_LOCK="seckill:upload:lock";

 /*
 幂等性处理
  1.加分布式锁，防止多个机器重复添加，只有获得锁的机器添加
  2.在业务逻辑内，添加过的商品不用重复添加
 */

  @Scheduled(cron = "* * 3 * * ?")
  public void uploadSeckillSkuLastest3Days(){
    //1.重复上架无需处理
    log.info("上架商品的秒杀信息");
    RLock lock = redissonClient.getLock(UPLOAD_LOCK);
    lock.lock(10, TimeUnit.SECONDS);
    try {
      seckillService.uploadSeckillSkuLastest3Days();
    } finally {
      lock.unlock();
    }

  }

}

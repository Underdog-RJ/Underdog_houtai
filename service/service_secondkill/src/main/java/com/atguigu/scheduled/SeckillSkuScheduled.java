package com.atguigu.scheduled;


import com.atguigu.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 秒杀商品的定时上架
 *    每天晚上3点：上架最近3天需要秒杀的商品
 */
@Slf4j
@Service
public class SeckillSkuScheduled {

  @Autowired
  SeckillService seckillService;

  // TODO 幂等性处理
  @Scheduled(cron = "0 * * * * ?")
  public void uploadSeckillSkuLastest3Days(){
    //1.重复上架无需处理
    log.info("上架商品的秒杀信息");
    seckillService.uploadSeckillSkuLastest3Days();
  }

}

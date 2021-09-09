package com.atguigu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("seckill/sec")
public class SeckillController {

  @Autowired
  SeckillService seckillService;

  @GetMapping("/getCurrentSeckillSkus")
  public Mono<R> getCurrentSeckillSkus(){
    return seckillService.getCurrentSeckillSkus();
  }

  /**
   * 查看当前sku 是否参与秒杀
   * @param id
   * @return
   */
  @GetMapping("/getSkuSeckillInfo/{id}")
  public R getSkuSeckillInfo(@PathVariable("id") String id){
    return seckillService.getSkuSeckillInfo(id);

  }

}

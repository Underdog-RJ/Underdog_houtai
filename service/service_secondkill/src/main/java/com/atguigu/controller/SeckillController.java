package com.atguigu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;


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

  /**
   * 参与秒杀
   */
  @GetMapping("/kill")
  public Mono<R> secKill(@RequestParam("killId") String killId,
                         @RequestParam("key") String key,
                         @RequestParam("num") Integer num,
                         HttpServletRequest request){

   String orderId= seckillService.kill(killId,key,num,request);
   return Mono.just(R.ok().data("orderId",orderId));
  }




}

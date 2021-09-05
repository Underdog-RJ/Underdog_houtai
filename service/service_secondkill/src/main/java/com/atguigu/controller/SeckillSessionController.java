package com.atguigu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSession;
import com.atguigu.service.SeckillSessionService;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("seckill/session")
public class SeckillSessionController {

  @Autowired
  private SeckillSessionService seckillSessionService;

  @PostMapping("saveSeckill")
  public Mono<R> saveSeckillSession(@RequestBody SeckillSession seckillSession){
    return seckillSessionService.save(seckillSession);
  }

  @GetMapping("getSeckillById/{id}")
  public Mono<R> getSeckillById(@PathVariable String id){
    return seckillSessionService.getSeckillById(id);
  }

  @GetMapping("getAllSeckills")
  public Mono<R> getAllSeckills(){
    return seckillSessionService.getAllSeckills();
  }

  @DeleteMapping("deleteSeckillById/{id}")
  public Mono<R> deleteSeckillById(@PathVariable String id){
    return seckillSessionService.delete(id);
  }

  @PutMapping("updateSeckill")
  public Mono<R> updateSeckill(@RequestBody SeckillSession seckillSession){
    return seckillSessionService.update(seckillSession);
  }

}

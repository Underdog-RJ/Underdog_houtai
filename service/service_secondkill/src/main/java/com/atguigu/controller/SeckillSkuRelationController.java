package com.atguigu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSession;
import com.atguigu.entity.SeckillSkuRelation;
import com.atguigu.service.SeckillSessionService;
import com.atguigu.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("seckill/sessionRelation")
public class SeckillSkuRelationController {

  @Autowired
  private SeckillSkuRelationService seckillSkuRelationService;

  @PostMapping("save")
  public Mono<R> save(@RequestBody SeckillSkuRelation seckillSkuRelation){
    return seckillSkuRelationService.save(seckillSkuRelation);
  }

  @GetMapping("{id}")
  public Mono<R> getById(@PathVariable String id){
    return seckillSkuRelationService.getById(id);
  }

  @GetMapping("getAll")
  public Mono<R> getAll(){
    return seckillSkuRelationService.getAll();
  }

  @DeleteMapping("deleteById/{id}")
  public Mono<R> delete(@PathVariable String id){
    return seckillSkuRelationService.delete(id);
  }

  @PutMapping("update")
  public Mono<R> update(@RequestBody SeckillSkuRelation seckillSkuRelation){
    return seckillSkuRelationService.update(seckillSkuRelation);
  }


}

package com.atguigu.service;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSession;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SeckillSessionService {


  Mono<R> save(SeckillSession seckillSession);

  Mono<R> getSeckillById(String id);

  Mono<R> getAllSeckills();

  Mono<R> delete(String id);

  Mono<R> update(SeckillSession seckillSession);

  Flux<SeckillSession> getLates3DaySession();
}

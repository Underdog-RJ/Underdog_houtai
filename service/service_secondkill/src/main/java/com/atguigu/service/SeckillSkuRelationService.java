package com.atguigu.service;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSkuRelation;
import reactor.core.publisher.Mono;

public interface SeckillSkuRelationService {
  Mono<R> save(SeckillSkuRelation seckillSkuRelation);

  Mono<R> getById(String id);

  Mono<R> getAll();

  Mono<R> delete(String id);

  Mono<R> update(SeckillSkuRelation seckillSkuRelation);
}

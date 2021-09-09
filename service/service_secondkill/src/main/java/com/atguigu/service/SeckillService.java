package com.atguigu.service;

import com.atguigu.commonutils.R;
import reactor.core.publisher.Mono;

public interface SeckillService {


  void uploadSeckillSkuLastest3Days();

  Mono<R> getCurrentSeckillSkus();

  R getSkuSeckillInfo(String id);
}

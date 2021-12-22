package com.atguigu.service;


import com.atguigu.commonutils.R;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;


public interface SeckillService {


  void uploadSeckillSkuLastest3Days();

  Mono<R> getCurrentSeckillSkus();

  R getSkuSeckillInfo(String id);

    String kill(String killId, String key, Integer num, HttpServletRequest request);
}

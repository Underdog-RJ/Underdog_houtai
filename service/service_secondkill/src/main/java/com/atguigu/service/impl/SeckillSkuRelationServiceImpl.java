package com.atguigu.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSkuRelation;
import com.atguigu.repository.SeckillSkuRelationRepository;
import com.atguigu.service.SeckillSkuRelationService;
import com.atguigu.servicebase.exception.BizCodeEnume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class SeckillSkuRelationServiceImpl implements SeckillSkuRelationService {

  @Autowired
  private SeckillSkuRelationRepository seckillSkuRelationRepository;

  @Override
  public Mono<R> save(SeckillSkuRelation seckillSkuRelation) {
    SeckillSkuRelation save = seckillSkuRelationRepository.save(seckillSkuRelation);
    return Mono.just(R.ok().data("seckillSkuRelation",save));
  }

  @Override
  public Mono<R> getById(String id) {
    Optional<SeckillSkuRelation> byId = seckillSkuRelationRepository.findById(Integer.parseInt(id));
    if(byId.isPresent()){
      return Mono.just(R.ok().data("session",byId.get()));
    }
    return Mono.just(R.error().message(BizCodeEnume.VALID_EXCEPTION.getMsg()));
  }

  @Override
  public Mono<R> getAll() {
    return Mono.just(R.ok().data("list",seckillSkuRelationRepository.findAll()));
  }

  @Override
  public Mono<R> delete(String id) {
    seckillSkuRelationRepository.deleteById(Integer.parseInt(id));
    return Mono.just(R.ok());
  }

  @Override
  public Mono<R> update(SeckillSkuRelation seckillSkuRelation) {
    SeckillSkuRelation save = seckillSkuRelationRepository.save(seckillSkuRelation);
    return Mono.just(R.ok().data("seckillSkuRelation",save));
  }
}

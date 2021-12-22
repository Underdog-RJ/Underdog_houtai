package com.atguigu.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.entity.SeckillSession;
import com.atguigu.repository.SeckillSessionRepository;
import com.atguigu.repository.SeckillSkuRelationRepository;
import com.atguigu.service.SeckillSessionService;

import com.atguigu.servicebase.exception.BizCodeEnume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeckillSessionServiceImpl implements SeckillSessionService {

  @Autowired
  private SeckillSessionRepository seckillSessionRepository;


  @Autowired
  private SeckillSkuRelationRepository seckillSkuRelationRepository;


  @Override
  public Mono<R> save(SeckillSession seckillSession) {
    SeckillSession session = seckillSessionRepository.save(seckillSession);
    return Mono.just(R.ok().data("session",session));
  }

  @Override
  public Mono<R> getSeckillById(String id) {
    Optional<SeckillSession> byId = seckillSessionRepository.findById(Integer.parseInt(id));
    if(byId.isPresent()){
      return Mono.just(R.ok().data("session",byId.get()));
    }
    return Mono.just(R.error().message(BizCodeEnume.VALID_EXCEPTION.getMsg()));
  }

  @Override
  public Mono<R> getAllSeckills() {
    List<SeckillSession> sessions = seckillSessionRepository.findAll();
    return Mono.just(R.ok().data("list",sessions));
  }

  @Override
  public Mono<R> delete(String id) {
    seckillSessionRepository.deleteById(Integer.parseInt(id));
    return Mono.just(R.ok());
  }

  @Override
  public Mono<R> update(SeckillSession seckillSession) {
    SeckillSession save = seckillSessionRepository.save(seckillSession);
    return Mono.just(R.ok().data("session",save));
  }

  @Override
  public Flux<SeckillSession> getLates3DaySession() {
    Map<String, String> startAndEndTime = getStartAndEndTime();
    List<SeckillSession> list = seckillSessionRepository.findSeckillSessionByStartTimeBetween(startAndEndTime.get("startTime"), startAndEndTime.get("endTime"));
    List<SeckillSession> collect=new ArrayList<>();
    if(!CollectionUtils.isEmpty(list)){
       collect = list.stream().map(session -> {
         Integer sessionId = session.getId();
         session.setRelationSkus(seckillSkuRelationRepository.findByPromotionSessionId(sessionId));
        return session;
      }).collect(Collectors.toList());
    }
    return Flux.fromIterable(collect);
  }

  public Map<String,String> getStartAndEndTime(){
    LocalDate now = LocalDate.now();
    LocalDate end = now.plusDays(2);


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");





    LocalTime min = LocalTime.MIN;
    LocalTime max = LocalTime.MAX;

    LocalDateTime startTime = LocalDateTime.of(now, min);
    LocalDateTime endTime = LocalDateTime.of(end, max);

    String startTimeStr = dateTimeFormatter.format(startTime);
    String endTimeStr = dateTimeFormatter.format(endTime);
    Map<String ,String> map=new HashMap<>();
    map.put("startTime",startTimeStr);
    map.put("endTime",endTimeStr);
    return map;

  }
}

package com.atguigu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.TimeUtil;
import com.atguigu.entity.SeckillSession;
import com.atguigu.entity.to.SeckillRedisTo;
import com.atguigu.entity.vo.SkuInfoVo;
import com.atguigu.feign.EduFeignService;
import com.atguigu.service.SeckillService;
import com.atguigu.service.SeckillSessionService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SeckillServiceImpl implements SeckillService {

  @Autowired
  SeckillSessionService seckillSessionService;

  @Autowired
  EduFeignService eduFeignService;

  @Autowired
  RedissonClient redissonClient;


  private final String SESSIONS_CACHE_PREFIX ="seckill:sessions:";

  private final String SKUKILL_CACHE_PREFIX= "seckill:skus";

  private final String SKU_STOCK_SEMAPHORE="seckill:stock"; // 商品随机码





  @Autowired
  StringRedisTemplate redisTemplate;
  @Override
  public void uploadSeckillSkuLastest3Days() {
    //1. 扫描最近三天需要参与秒杀的活动

    //  上架商品
    Flux<SeckillSession> lates3DaySession = seckillSessionService.getLates3DaySession();

    // 缓存到redis中
    // 1. 缓存活动信息
    saveSessionInfos(lates3DaySession);
    // 2.缓存活动的关联商品信息
    saveSessionSkuInfos(lates3DaySession);

  }

  private void saveSessionInfos(Flux<SeckillSession> sessions){
    sessions.toIterable().forEach((session->{
      Long startTime = TimeUtil.timeStrWithPattern2Long(session.getStartTime());
      Long endTime = TimeUtil.timeStrWithPattern2Long(session.getEndTime());
      String key= SESSIONS_CACHE_PREFIX +startTime+"_"+ endTime;
      List<String> collect = session.getRelationSkus().stream().map(item -> item.getSkuId()).collect(Collectors.toList());
      //缓存活动信息
      redisTemplate.opsForList().leftPushAll(key,collect);
    }));
  }

  private void saveSessionSkuInfos(Flux<SeckillSession> sessions){


    sessions.toIterable().forEach((session->{
      BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
      session.getRelationSkus().stream().forEach(seckillSkuRelation -> {
        // 缓存商品
        SeckillRedisTo redisTo = new SeckillRedisTo();
        //1.sku的基本信息
     /*   R courseInfoResult = eduFeignService.getCourseInfo(seckillSkuRelation.getSkuId());

        if(Objects.equals(20000,courseInfoResult.getCode())){
          Map<String, Object> data = courseInfoResult.getData();
          if(data.containsKey("courseInfoVo")){
            SkuInfoVo courseInfoVo = JSON.parseObject((String) data.get("courseInfoVo"), new TypeReference<SkuInfoVo>() {});
            redisTo.setSkuInfoVo(courseInfoVo);
          }
        }*/
        //2.sku的秒杀信息
        BeanUtils.copyProperties(seckillSkuRelation,redisTo);

        //3. 设置当前商品的秒杀时间信息
        redisTo.setStartTime(TimeUtil.timeStrWithPattern2Long(session.getStartTime()));
        redisTo.setEndTime(TimeUtil.timeStrWithPattern2Long(session.getEndTime()));

        // 4. 设置随机码
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTo.setRandomCode(token);

        // 使用可以秒杀的个数作为分布式的信号量  限流
        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);

        // 商品可以秒杀的数量最为信号量
        semaphore.trySetPermits(seckillSkuRelation.getSeckillCount());

        String jsonString = JSON.toJSONString(redisTo);

        ops.put(seckillSkuRelation.getSkuId(),jsonString);
      });
    }));


  }
}

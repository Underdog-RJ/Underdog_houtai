package com.atguigu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.TimeUtil;
import com.atguigu.entity.SeckillSession;
import com.atguigu.entity.SeckillSkuRelation;
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
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Pattern;
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

  private final String SKU_STOCK_SEMAPHORE="seckill:stock:"; // 商品随机码





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
      Boolean hasKey = redisTemplate.hasKey(key);
      if(!hasKey){
        //缓存活动信息
        List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "_" + item.getSkuId()).collect(Collectors.toList());
        redisTemplate.opsForList().leftPushAll(key,collect);
      }
    }));
  }

  private void saveSessionSkuInfos(Flux<SeckillSession> sessions){

    sessions.toIterable().forEach((session->{
      BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
      session.getRelationSkus().stream().forEach(seckillSkuRelation -> {

        String token = UUID.randomUUID().toString().replace("-", "");

        if(!ops.hasKey(seckillSkuRelation.getPromotionSessionId()+"_"+seckillSkuRelation.getSkuId())){

          // 缓存商品
          SeckillRedisTo redisTo = new SeckillRedisTo();
          //1.sku的基本信息
          R courseInfoResult = eduFeignService.getCourseInfo(seckillSkuRelation.getSkuId());

          if(Objects.equals(20000,courseInfoResult.getCode())){
            Map<String, Object> data = courseInfoResult.getData();
            if(data.containsKey("courseInfoVo")){
              SkuInfoVo courseInfoVo = JSON.parseObject(JSON.toJSONString(data.get("courseInfoVo")), new TypeReference<SkuInfoVo>() {});
              redisTo.setSkuInfoVo(courseInfoVo);
            }
          }
          //2.sku的秒杀信息
          BeanUtils.copyProperties(seckillSkuRelation,redisTo);

          //3. 设置当前商品的秒杀时间信息
          redisTo.setStartTime(TimeUtil.timeStrWithPattern2Long(session.getStartTime()));
          redisTo.setEndTime(TimeUtil.timeStrWithPattern2Long(session.getEndTime()));
          // 4. 设置随机码
          redisTo.setRandomCode(token);

          // 5. 存入redis中
          String jsonString = JSON.toJSONString(redisTo);
          ops.put(seckillSkuRelation.getPromotionSessionId() + "_" +  seckillSkuRelation.getSkuId(),jsonString);

          //6. 上架库存信息
          // 使用可以秒杀的个数作为分布式的信号量  限流
          RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
          // 商品可以秒杀的数量最为信号量
          semaphore.trySetPermits(seckillSkuRelation.getSeckillCount());
        }

      });
    }));


  }

    /**
     * 当前可以参与秒杀的商品信息
     * @return
     */
    @Override
    public Mono<R> getCurrentSeckillSkus() {

      //1.去欸的那个当前时间水域那个秒杀场次
      long now = new Date().getTime()/1000;
      Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
      for (String key : keys) {
        String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
        String[] s = replace.split("_");
        long startTime = Long.parseLong(s[0]);
        long endTime = Long.parseLong(s[1]);
        if(now>=startTime&&now<=endTime){
          List<String> range = redisTemplate.opsForList().range(key, 0, -1);
          BoundHashOperations<String, String, Object> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
          List<Object> objects = hashOps.multiGet(range);
          if(!CollectionUtils.isEmpty(objects)){
            List<SeckillRedisTo> collect = objects.stream().map(item -> {
              SeckillRedisTo redisTo = JSON.parseObject(item.toString(), SeckillRedisTo.class);
              redisTo.setRandomCode(null); // 当前秒杀开始就需要随机码
              return redisTo;
            }).collect(Collectors.toList());
            return Mono.just(R.ok().data("list",collect));
          }
          break;
        }
      }

      return Mono.empty();
    }


  @Override
  public R getSkuSeckillInfo(String id) {
    //1.找到所有需要参与秒杀的商品的key
    BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

    Set<String> keys = hashOps.keys();
    if(!CollectionUtils.isEmpty(keys)){
      // 定义正则
//      String regx="\\d"+id;
      for (String key : keys) {
        String[] s = key.split("_");
        if(Objects.equals(s[1],id)){
          String json = hashOps.get(key);
          SeckillRedisTo seckillRedisTo = JSON.parseObject(json, SeckillRedisTo.class);
          // 处理随机码
          long now = new Date().getTime() / 1000;
          if(now>=seckillRedisTo.getStartTime()&&now<=seckillRedisTo.getEndTime()){
          }else {
            seckillRedisTo.setRandomCode(null);
          }
          return R.ok().data("edu",seckillRedisTo);
        }
      }
    }

    return null;
  }
}

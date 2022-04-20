package com.atguigu.pierce.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {

  /**
   * 所有对Redission的使用都是通过RedissionClient对象
   可以在redission官网获取更多的配置信息
   */
  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson(){
    //1.创建配置
    Config config=new Config();
    config.useSingleServer().setAddress("redis://101.35.143.19:6379").setDatabase(0).setPassword("zhangzhengxu123.");
    //2.根据config创建出RedissonClient示例
    RedissonClient redissonClient = Redisson.create(config);
    return redissonClient;
  }
}
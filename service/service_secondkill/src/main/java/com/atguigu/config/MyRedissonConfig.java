package com.atguigu.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {

  /**
   * 所有对Redission的使用都是通过RedissionClient对象
   */
  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson(){
    //1.创建配置
    Config config=new Config();
    config.useSingleServer().setAddress("redis://10.1.1.147:6379").setDatabase(0);
    //2.根据config创建出RedissonClient示例
    RedissonClient redissonClient = Redisson.create(config);
    return redissonClient;
  }
}

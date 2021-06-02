package com.atguigu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.atguigu")
@ComponentScan("com.atguigu")
public class LivingApplication {
    public static void main(String[] args) {
        SpringApplication.run(LivingApplication.class);
    }
}

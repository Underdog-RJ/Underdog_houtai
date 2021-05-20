package com.atguigu.eduservice;

import com.atguigu.eduservice.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"}) //指定扫描位置
@MapperScan("com.atguigu.eduservice.mapper")
public class ChatApplicaion {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplicaion.class);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}

package com.atguigu.eduservice;

import com.atguigu.eduservice.entity.ImageRecoGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.atguigu"})
@EnableDiscoveryClient //nacos注册
@EnableScheduling
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }

    @Bean
    public ImageRecoGrpc.ImageRecoBlockingStub getBlock() {
        // usePlaintext表示明文传输，否则需要配置ssl, channel  表示通信通道
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 17890).usePlaintext().build();
        // 存根，用于调用服务端的接口方法。
        ImageRecoGrpc.ImageRecoBlockingStub blockingStub = ImageRecoGrpc.newBlockingStub(channel);

        return blockingStub;
    }
}

package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-secondkill")
public interface SeckillClient {

  @GetMapping("/seckill/sec/getSkuSeckillInfo/{id}")
   R getSkuSeckillInfo(@PathVariable("id") String id);
}

package com.atguigu.educenter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-edu")
public interface EnjoyBlogClient {

    @GetMapping("/eduservice/blogEnjoy/getEnjoyCount/{userId}")
    Integer EnjoyBlogCountById(@PathVariable("userId") String userId);

}

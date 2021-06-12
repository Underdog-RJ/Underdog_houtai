package com.atguigu.eduservice.client;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.User;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "service-acl")
public interface AclUserClient {

    @PostMapping("/admin/acl/user/save")
    R save(@RequestBody User user,  @RequestHeader(name = "token", required = true) String token);

}

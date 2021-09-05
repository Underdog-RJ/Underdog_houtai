package com.atguigu.eduorder.client;



import com.atguigu.eduorder.entity.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @PostMapping("/educenter/member/getUserInfoOrder/{id}")
    UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);

    @GetMapping("/educenter/member/updateUseruCoin/{count}")
    boolean updateUseruCoin(@PathVariable("count") Integer count, @RequestHeader(name = "token", required = true) String token);

}

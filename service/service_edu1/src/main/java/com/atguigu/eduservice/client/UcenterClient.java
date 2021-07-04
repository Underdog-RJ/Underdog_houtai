package com.atguigu.eduservice.client;

import com.atguigu.eduservice.entity.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    //根据用户id获取用户信息
    @GetMapping("/educenter/member/getUcenterPay/{memberId}")
    UcenterMemberPay getUcenterPay(@PathVariable("memberId") String memberId);


    @GetMapping("/educenter/member/updateUseruCoin/{count}")
    boolean updateUseruCoin(@PathVariable("count") Integer count, @RequestHeader(name = "token", required = true) String token);

}

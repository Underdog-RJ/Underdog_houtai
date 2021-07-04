package com.atguigu.eduservice.client;

import com.atguigu.eduservice.entity.UcenterMemberPay;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public UcenterMemberPay getUcenterPay(String memberId) {
        return null;
    }

    @Override
    public boolean updateUseruCoin(Integer count, String token) {
        return false;
    }
}

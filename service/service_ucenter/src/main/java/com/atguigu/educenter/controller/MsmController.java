package com.atguigu.educenter.controller;

import com.atguigu.commonutils.R;

import com.atguigu.educenter.service.MsmService;
import com.atguigu.educenter.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/educenter/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone)
    {
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code",code);
        //调用service发送短信的方法
        boolean isSend=msmService.send(param,phone);
        if(isSend){
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.HOURS);
            return R.ok();

        }else {
            return R.error().message("发送短信失败");
        }
    }

}

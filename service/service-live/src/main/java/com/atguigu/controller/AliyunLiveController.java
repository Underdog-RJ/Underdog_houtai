package com.atguigu.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.config.AliyunLiveConfig;
import com.atguigu.entity.EduLiving;
import com.atguigu.service.EduLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.atguigu.util.AliyunLiveUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/live/aliyunLive")
public class AliyunLiveController {

    @Autowired
    private EduLivingService eduLivingService;

    @PostMapping("addLive")
    public R addLive(@RequestBody EduLiving eduLiving, HttpServletRequest request) {

        String teacherId = JwtUtils.getMemberIdByJwtToken(request);


        return eduLivingService.addLive(teacherId,eduLiving);


    }

    @GetMapping("getLive/{courseId}")
    public R getLive(@PathVariable Integer courseId) {

        /**
         * 注意，推流要在播流域名里面生成
         */

//        Map<String, String> pullUrl = AliyunLiveUtil.createPullUrl(courseId,  aliyunLiveConfig);
//
//        return R.ok().data("pullUrl",pullUrl);

        System.out.println(eduLivingService);
        eduLivingService.getLive();
        return null;
    }

}

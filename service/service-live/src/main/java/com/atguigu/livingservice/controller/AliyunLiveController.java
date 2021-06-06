package com.atguigu.livingservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;

import com.atguigu.livingservice.entity.EduLiving;
import com.atguigu.livingservice.service.EduLivingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/live/aliyunLive")
public class AliyunLiveController {

    @Autowired
    private EduLivingService eduLivingService;


    /**
     * 获取当前全部直播列表
     * @param
     * @param
     * @return
     */
    @GetMapping("getAllLiving")
    public R getAllLiving(){
       List<EduLiving> list= eduLivingService.getAllLivingList();
       return R.ok().data("list",list);
    }



    @PostMapping("addLive")
    public R addLive(@RequestBody EduLiving eduLiving, HttpServletRequest request) {

        Map<String, String> userInfo = JwtUtils.getUserIdByJwtToken(request);

        return eduLivingService.addLive(userInfo,eduLiving);


    }

    @GetMapping("getLive")
    public R getLive(HttpServletRequest request) {

        String userId = JwtUtils.getMemberIdByJwtToken(request);

        List<EduLiving> eduLivingList= eduLivingService.getLiveById(userId);

        return R.ok().data("eduLiving",eduLivingList);
    }

    @GetMapping("getLiveById/{teacherId}")
    public R getLiveById(@PathVariable String teacherId) {

        EduLiving livingInfo= eduLivingService.getLiveInfoById(teacherId);

        return R.ok().data("livingInfo",livingInfo);
    }

    @DeleteMapping("removeLivingById/{teacherId}")
    public R removeLivingById(@PathVariable String teacherId){

       return eduLivingService.removeLivingById(teacherId);

    }

}

package com.atguigu.livingservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;

import com.atguigu.livingservice.entity.EduLiving;
import com.atguigu.livingservice.service.EduLivingService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/live/aliyunLive")
public class AliyunLiveController {

    @Autowired
    private EduLivingService eduLivingService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /*
    往期直播
     */

    @GetMapping("getAllLivingAge")
    public R getAllLivingAge(){
        List<EduLiving> eduLivingList=eduLivingService.getAllLivingAge();
        return  R.ok().data("eduLivingList",eduLivingList);
    }

    /**
     * 设置回放地址
     */
    @PostMapping("uploadAlyiVideo/{currendLivingId}")
    public R uploadAlyiVideo(MultipartFile file,@PathVariable String currendLivingId,HttpServletRequest request){
        return   eduLivingService.uploadAlyiVideo(file,currendLivingId,request);
    }

    /**
     * 删除回访地址
     */
    @DeleteMapping("deleteAliyunvod/{id}")
    public R deleteAliyunvod(@PathVariable String id,HttpServletRequest request){
        return  eduLivingService.deleteAliyunvod(id,request);
    }


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

    @GetMapping("getLiveByIdAgo/{id}")
    public R getLiveByIdAgo(@PathVariable String id) {
        return eduLivingService.getLiveInfoByIdAgo(id);
    }

    @DeleteMapping("removeLivingById")
    public R removeLivingById(@RequestBody EduLiving eduLiving){
        rabbitTemplate.convertAndSend("living-event-exchange","living.release.living",eduLiving.getId());
         return eduLivingService.removeLivingById(eduLiving.getTeacherId());

    }

    @GetMapping("getAllRecordById/{id}")
    public R getAllRecordById(@PathVariable String id,HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        return eduLivingService.getAllRecordById(id,memberId);
    }

}

package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    @GetMapping("{id}")
    public R getVideoById(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        if(StringUtils.isEmpty(eduVideo)){
            return R.error().message("未找到该视频");
        }
        return R.ok().data("eduVideo",eduVideo);
    }


    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){

        eduVideoService.save(eduVideo);
        return R.ok();
    }
    //删除小节
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id湖区视频id调用方法实现视频删除
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();


        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id，远程调用是啊先视频删除
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败，熔断器");
            }
        }
        //删除小节
        eduVideoService.removeById(id);
        return R.ok();
    }

    //修该小节 TODO
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

}


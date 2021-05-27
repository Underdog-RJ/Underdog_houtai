package com.atguigu.educenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterKecheng;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterKechengService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
@RestController
@RequestMapping("/educenter/ucenter-kecheng")
public class UcenterKechengController{
    @Autowired
    UcenterKechengService ucenterKechengService;
    //根据用户ID和课程ID查询课程是否被用户收藏
    @GetMapping("iscollect")
    public R isCollect(@RequestParam("userId") String userId,@RequestParam("courseId") String courseId ){
        int flag=0;
        QueryWrapper<UcenterKecheng> kechengQueryWrapper = new QueryWrapper<>();
        kechengQueryWrapper.eq("user_id",userId)
                            .eq("course_id",courseId);
        UcenterKecheng one = ucenterKechengService.getOne(kechengQueryWrapper);
        if (one != null) {
            flag=one.getIsCollect();
        }
        return R.ok().data("flag",flag);
    }
    //设置是否收藏
    @GetMapping("set_collect")
    public R setCollect(@RequestParam("userId") String userId,@RequestParam("courseId") String courseId,@RequestParam("flag") Integer flag){
        QueryWrapper<UcenterKecheng> kechengQueryWrapper = new QueryWrapper<>();
        kechengQueryWrapper.eq("user_id",userId)
                .eq("course_id",courseId);
        UcenterKecheng ucenterKecheng = new UcenterKecheng();
        ucenterKecheng.setIsCollect(flag);
        boolean update = ucenterKechengService.update(ucenterKecheng, kechengQueryWrapper);
        return R.ok().success(update);
    }
    //根据用户ID获得所有收藏
    @GetMapping("get_collect")
    public R getCollectById(@RequestParam("userId") String userId){
        QueryWrapper<UcenterKecheng> kechengQueryWrapper = new QueryWrapper<>();
        kechengQueryWrapper.eq("user_id",userId);
        List<UcenterKecheng> list = ucenterKechengService.list(kechengQueryWrapper);

        return R.ok().data("list",list);
    }

}


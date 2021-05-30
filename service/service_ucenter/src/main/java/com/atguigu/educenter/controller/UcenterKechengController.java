package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.EduCourse;
import com.atguigu.educenter.entity.UcenterKecheng;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.UcenterShuoshuo;
import com.atguigu.educenter.service.UcenterKechengService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("is_collect")
    public R isCollect(@RequestParam("courseId") String courseId,HttpServletRequest request){
        int flag=0;
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        System.out.println("userID"+userId);
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
    public R setCollect(@RequestParam("courseId") String courseId, @RequestParam("flag") Integer flag,HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        System.out.println("userID123"+userId);
        System.out.println("courseId"+courseId);
        QueryWrapper<UcenterKecheng> kechengQueryWrapper = new QueryWrapper<>();
        kechengQueryWrapper.eq("user_id",userId)
                .eq("course_id",courseId);
        UcenterKecheng ucenterKecheng = new UcenterKecheng();
        if (flag == 1) {
            flag=0;
        }else {
            flag=1;
        }
        ucenterKecheng.setIsCollect(flag);
        boolean update;
        int i = ucenterKechengService.count(kechengQueryWrapper);
        if (i==0) {
            ucenterKecheng.setUserId(userId).setCourseId(courseId);
            update = ucenterKechengService.save(ucenterKecheng);
        }else {
            update = ucenterKechengService.update(ucenterKecheng, kechengQueryWrapper);
        }
        return R.ok().success(update);
    }

    //根据用户ID获得所有收藏
    @GetMapping("get_collect/{current}/{limit}")
    public R getCollectById(@PathVariable Long current,@PathVariable Long limit,HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        QueryWrapper<UcenterKecheng> wrapper = new QueryWrapper<>();
        Page<EduCourse> page=new Page<>(current,limit);
        wrapper.eq("user_id",userId);
        wrapper.eq("is_collect",1);
        wrapper.orderByDesc("gmt_create");
        ucenterKechengService.pageList( page, userId);
        List<EduCourse> records = page.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);
    }
    @GetMapping("get_pay/{current}/{limit}")
    public R getPayById(@PathVariable Long current,@PathVariable Long limit,HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        QueryWrapper<UcenterKecheng> wrapper = new QueryWrapper<>();
        Page<EduCourse> page=new Page<>(current,limit);
        wrapper.eq("user_id",userId);
        wrapper.orderByDesc("gmt_create");
        ucenterKechengService.pagePayList( page, userId);
        List<EduCourse> records = page.getRecords();
        for (EduCourse e:records
             ) {
            System.out.println("Educrouse++++:"+e.toString());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);
    }


    //根据用户ID获得所有收藏
    @GetMapping("findCollectById/{userId}/{current}/{limit}")
    public R findCollectById(@PathVariable String userId,@PathVariable Long current,@PathVariable Long limit,HttpServletRequest request){
        QueryWrapper<UcenterKecheng> wrapper = new QueryWrapper<>();
        Page<EduCourse> page=new Page<>(current,limit);
        wrapper.eq("user_id",userId);
        wrapper.eq("is_collect",1);
        wrapper.orderByDesc("gmt_create");
        ucenterKechengService.pageList( page, userId);
        List<EduCourse> records = page.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);
    }

    @GetMapping("findPayById/{userId}/{current}/{limit}")
    public R findPayById(@PathVariable String userId,@PathVariable Long current,@PathVariable Long limit){
        QueryWrapper<UcenterKecheng> wrapper = new QueryWrapper<>();
        Page<EduCourse> page=new Page<>(current,limit);
        wrapper.eq("user_id",userId);
        wrapper.orderByDesc("gmt_create");
        ucenterKechengService.pagePayList( page, userId);
        List<EduCourse> records = page.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return R.ok().data(map);
    }



}


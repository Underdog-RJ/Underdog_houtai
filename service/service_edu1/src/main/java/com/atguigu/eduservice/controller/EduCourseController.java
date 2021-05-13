package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.commonutils.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //课程列表，基本实现
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R getCourseList(@PathVariable Long current, @PathVariable Long limit, @RequestBody(required = false)CourseQuery courseQuery){
        Page<EduCourse> pageCourse=new Page<>(current,limit);
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();

        String status = courseQuery.getStatus();
        String title = courseQuery.getTitle();
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //分页查询
        courseService.page(pageCourse, wrapper);
        List<EduCourse> list = pageCourse.getRecords();
        long total = pageCourse.getTotal();
        return R.ok().data("list",list).data("total",total);
    }

    //添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id=courseService.saveCourseInfo(courseInfoVo);

        return R.ok().data("courseId",id);
    }

    //根据课程查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId) {
       CourseInfoVo courseInfoVo= courseService.getCourseInfo(courseId);
       return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修该课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo=courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);

    }
    //课程最终发布
    //修该课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id)
    {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId)
    {
        courseService.removeCourse(courseId);
        return R.ok();
    }

}


package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduBlogTop;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.EduBlogTopService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduBlogService eduBlogService;

    @Autowired
    private EduBlogTopService eduBlogTopService;

    //查询前8条热门记录，查询前4条名师
    @GetMapping("index")
    public R index(){
        //查询前8条热门记录
        QueryWrapper<EduCourse> wrapperCourse=new QueryWrapper<>();
        wrapperCourse.orderByDesc("id");
        wrapperCourse.last("limit 8");
        List<EduCourse> eduList = courseService.list(wrapperCourse);

        //查询前4条名师
        QueryWrapper<EduTeacher> wrapperTeacher=new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> teacherList = eduTeacherService.list(wrapperTeacher);

        //查询前8条热门微博
        QueryWrapper<EduBlogTop> wrapperBlog=new QueryWrapper<>();
        wrapperBlog.orderByDesc("gmt_create");
        wrapperBlog.last("limit 8");

        List<EduBlogTop> blogList = eduBlogTopService.list(wrapperBlog);

        return R.ok().data("eduList",eduList).data("teacherList",teacherList).data("blogList",blogList);
    }


}

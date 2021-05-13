package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrdersClient;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.impl.EduBlogServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/Blogfront")
public class BlogFontController {

    @Autowired
    private EduBlogService eduBlogService;

    //1 条件查询带分页查询课程
    @PostMapping("getFrontBlogList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit, @RequestBody(required = false) BlogQuery blogQuery){
        Map<String,Object> map=eduBlogService.findBypageFront(page, limit, blogQuery);
        //返回分页所有数据
        return R.ok().data(map);

    }

    //2博客详情的方法
    @GetMapping("getFrontBlogInfo/{id}")
    public R getFrontCourseInfo(@PathVariable("id") String courseId){
        EduBlog byId = eduBlogService.getById(courseId);
        return R.ok().data("eduBlog", byId);

    }



}

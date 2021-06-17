package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrdersClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrdersClient ordersClient;

    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit, @RequestBody(required = false)CourseFrontVo courseFrontVo){
        Page<EduCourse> pageCourse=new Page<>(page,limit);

        Map<String,Object> map= eduCourseService.getCourseFrontList(pageCourse,courseFrontVo);

        //返回分页所有数据
        return R.ok().data(map);

    }

    //2课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo=eduCourseService.getBaseCourseInfo(courseId);

        courseWebVo.setViewCount(courseWebVo.getViewCount()+1);

        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseWebVo,eduCourse);
        eduCourseService.updateById(eduCourse);


        //根据课程Id查询章节和小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);
        Cookie[] cookies = request.getCookies();
        String token = request.getHeader("token");

        //根据课程id和用户id查询当前课程是否已经支付过了
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId))
        {
            return R.ok().code(28004);
        }
        boolean buyCourse = ordersClient.isBuyCourse(courseId,memberId);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }

    //根据课程id查询课程信息
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id)
    {
        CourseWebVo courseInfo = eduCourseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }

    //根据课程Id更新课程的购买数量
    @GetMapping("updateBuyCount/{id}")
    public void updateBuyCount(@PathVariable String id)
    {
        EduCourse byId = eduCourseService.getById(id);
        byId.setBuyCount(byId.getBuyCount()+1);
        eduCourseService.updateById(byId);
    }


}

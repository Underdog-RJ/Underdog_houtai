package com.atguigu.eduservice.controller.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.eduservice.client.OrdersClient;
import com.atguigu.eduservice.client.SeckillClient;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.to.SeckillRedisTo;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrdersClient ordersClient;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private SeckillClient seckillClient;

    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page,
                                @PathVariable long limit,
                                @RequestBody(required = false)CourseFrontVo courseFrontVo,
                                HttpServletRequest request){

        String token = request.getHeader("token");
        Page<EduCourse> pageCourse=new Page<>(page,limit);

        Map<String,Object> map= eduCourseService.getCourseFrontList(pageCourse,courseFrontVo);

        //返回分页所有数据
        return R.ok().data(map);

    }

    //2课程详情的方法
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) throws ExecutionException, InterruptedException {


        CompletableFuture<CourseWebVo> futureCouseWebVo = CompletableFuture.supplyAsync(() -> {
            //1.根据课程id，编写sql语句查询课程信息
            CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(courseId);
            return courseWebVo;
        }, executor);

        //等待futureCouseWebVo完成之后在执行
        CompletableFuture<Void> futureUpdate = futureCouseWebVo.thenAcceptAsync((res) -> {
            //2.更新浏览量
            res.setViewCount(res.getViewCount() + 1);
            EduCourse eduCourse = new EduCourse();
            BeanUtils.copyProperties(res, eduCourse);
            eduCourseService.updateById(eduCourse);
        }, executor);


        CompletableFuture<List<ChapterVo>> futureChapterVideoList = CompletableFuture.supplyAsync(() -> {
            //3.根据课程Id查询章节和小节
            List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);
            return chapterVideoList;
        }, executor);

        CompletableFuture<Object> futureBuyCouse = CompletableFuture.supplyAsync(() -> {
            //4.根据课程id和用户id查询当前课程是否已经支付过了
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            if (StringUtils.isEmpty(memberId)) {
                return R.ok().code(28004);
            }
            boolean buyCourse = ordersClient.isBuyCourse(courseId, memberId);
            return buyCourse;
        }, executor);

        // 查询当前商品是否参与秒杀

      CompletableFuture<SeckillRedisTo> isPromote = CompletableFuture.supplyAsync(() -> {
        R result = seckillClient.getSkuSeckillInfo(courseId);
        SeckillRedisTo edu = null;
        if (result.getCode() == 20000) {
          edu = JSON.parseObject(JSON.toJSONString(result.getData().get("edu")), new TypeReference<SeckillRedisTo>() {
          });
        }
        return edu;
      }, executor);


      CompletableFuture.allOf(futureUpdate,futureChapterVideoList,futureBuyCouse,isPromote).get();


      return R.ok().data("courseWebVo",futureCouseWebVo.get()).data("chapterVideoList",futureChapterVideoList.get()).data("isBuy",futureBuyCouse.get()).data("isPromote",isPromote);
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

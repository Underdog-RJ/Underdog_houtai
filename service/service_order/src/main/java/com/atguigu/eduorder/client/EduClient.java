package com.atguigu.eduorder.client;

import com.atguigu.commonutils.order.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {


    //根据课程id查询课程信息
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
    CourseWebVoOrder getCourseInfoOrder(@PathVariable("id") String id);


    //根据课程Id更新课程的购买数量
    @GetMapping("/eduservice/coursefront/updateBuyCount/{id}")
    void updateBuyCount(@PathVariable("id") String id);



}

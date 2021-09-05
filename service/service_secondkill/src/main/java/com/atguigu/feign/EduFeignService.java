package com.atguigu.feign;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
public interface EduFeignService {


  //根据课程查询课程基本信息
  @GetMapping("/eduservice/course/getCourseInfo/{courseId}")
  R getCourseInfo(@PathVariable("courseId") String courseId);
}

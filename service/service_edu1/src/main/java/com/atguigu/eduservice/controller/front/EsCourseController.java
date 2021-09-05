package com.atguigu.eduservice.controller.front;

import com.atguigu.eduservice.entity.CourseSearchParam;
import com.atguigu.eduservice.service.EsCourseService;
import com.atguigu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/coursefront")
public class EsCourseController {


    @Autowired
    EsCourseService esCourseService;


    @GetMapping("seachList/{page}/{size}")
    public R list(@PathVariable int page,@PathVariable int size, CourseSearchParam courseSearchParam){

        return esCourseService.list(page,size,courseSearchParam);

    }


}

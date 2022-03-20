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

    @GetMapping("searchTop")
    public R searchTop(){
        return esCourseService.searchTop();
    }
    @PutMapping("updateKeyWord")
    public R updateKeyWord(String keyword){
        return esCourseService.updateKeyWord(keyword);
    }


    @PostMapping("seachList/{page}/{size}")
    public R list(@PathVariable int page,@PathVariable int size, CourseSearchParam courseSearchParam){

        return esCourseService.list(page,size,courseSearchParam);

    }


}

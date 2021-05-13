package com.atguigu.eduservice.controller;


import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {


    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        subjectService.addSubject(file,subjectService);
        return R.ok();
    }

    //课程分类列表(树形)
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合反省是一级分类
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }


}


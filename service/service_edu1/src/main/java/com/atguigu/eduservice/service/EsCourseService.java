package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.CourseSearchParam;
import com.atguigu.commonutils.R;

public interface EsCourseService {
    R list(int page, int size, CourseSearchParam courseSearchParam);

    R searchTop();


    R updateKeyWord(String keyword);

    R allSearch(String keyword);
}

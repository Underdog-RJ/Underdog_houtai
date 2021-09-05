package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduPub;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {


    CoursePublishVo getPublishCourseInfo(String courseId);


    CourseWebVo getBaseCourseInfo(String courseId);

}

package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.EduCourse;
import com.atguigu.educenter.entity.UcenterKecheng;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
public interface UcenterKechengService extends IService<UcenterKecheng> {

    IPage<EduCourse> pageList(Page<EduCourse> page, String userId);
}

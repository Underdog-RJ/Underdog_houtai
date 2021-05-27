package com.atguigu.educenter.service.impl;

import com.atguigu.educenter.entity.EduCourse;
import com.atguigu.educenter.entity.UcenterKecheng;
import com.atguigu.educenter.mapper.UcenterKechengMapper;
import com.atguigu.educenter.service.UcenterKechengService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
@Service
public class UcenterKechengServiceImpl extends ServiceImpl<UcenterKechengMapper, UcenterKecheng> implements UcenterKechengService {
    @Autowired
    UcenterKechengMapper ucenterKechengMapper;
    public IPage<EduCourse> pageList(Page<EduCourse> page, String userId){
        return ucenterKechengMapper.pageList(page,userId);
    };
}

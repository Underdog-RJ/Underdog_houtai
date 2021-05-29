package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.EduCourse;
import com.atguigu.educenter.entity.UcenterKecheng;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-26
 */
@Mapper
public interface UcenterKechengMapper extends BaseMapper<UcenterKecheng> {

    IPage<EduCourse> pageList(Page<EduCourse> page, String userId);

    IPage<EduCourse> pagePayList(Page<EduCourse> page, String userId);
}

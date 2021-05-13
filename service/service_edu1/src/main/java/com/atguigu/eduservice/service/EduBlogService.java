package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-03
 */
public interface EduBlogService extends IService<EduBlog> {

    IPage<EduBlog> findByPage(Long page, Long limit, BlogQuery blogQuery);

    Map<String, Object> findBypageFront(long page, long limit, BlogQuery blogQuery);
}

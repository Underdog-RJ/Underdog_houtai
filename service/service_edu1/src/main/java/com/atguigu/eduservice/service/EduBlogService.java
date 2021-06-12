package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    IPage<EduBlog> findByPage(Long page, Long limit, BlogQuery blogQuery, HttpServletRequest request);

    Map<String, Object> findBypageFront(long page, long limit, BlogQuery blogQuery);

    List<EduBlog> getBlogByUserId(String id);
}

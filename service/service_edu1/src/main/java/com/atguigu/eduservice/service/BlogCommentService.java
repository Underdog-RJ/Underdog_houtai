package com.atguigu.eduservice.service;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-04-24
 */
public interface BlogCommentService extends IService<BlogComment> {

    R addCommit(BlogComment comment, HttpServletRequest request);
}

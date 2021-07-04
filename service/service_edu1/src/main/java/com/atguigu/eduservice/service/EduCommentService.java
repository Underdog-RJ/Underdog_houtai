package com.atguigu.eduservice.service;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-31
 */
public interface EduCommentService extends IService<EduComment> {

    R addCommit(EduComment comment, HttpServletRequest request);
}

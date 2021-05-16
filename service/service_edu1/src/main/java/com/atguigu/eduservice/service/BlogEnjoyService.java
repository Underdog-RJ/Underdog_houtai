package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.BlogEnjoy;
import com.atguigu.eduservice.entity.EduBlog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-15
 */
public interface BlogEnjoyService extends IService<BlogEnjoy> {

    boolean addEnjoy(String id, String userId);

    boolean isEnjoy(String id, String userId);

    boolean RemoveEnjoyBlog(String id, String userId);

    List<EduBlog> enjoyList(String userId);
}

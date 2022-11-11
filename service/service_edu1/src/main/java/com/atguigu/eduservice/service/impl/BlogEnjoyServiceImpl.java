package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.BlogEnjoy;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.mapper.BlogEnjoyMapper;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.service.BlogEnjoyService;
import com.atguigu.servicebase.entity.PageObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-15
 */
@Service
public class BlogEnjoyServiceImpl extends ServiceImpl<BlogEnjoyMapper, BlogEnjoy> implements BlogEnjoyService {

    @Autowired
    public BlogEnjoyMapper blogEnjoyMapper;

    @Autowired
    public EduBlogMapper eduBlogMapper;

    @Override
    public boolean addEnjoy(String id, String userId) {
        BlogEnjoy enjoy = new BlogEnjoy();
        enjoy.setBlogId(id);
        enjoy.setUserId(userId);
        int insert = blogEnjoyMapper.insert(enjoy);
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isEnjoy(String id, String userId) {
        Integer flag = blogEnjoyMapper.isEnjoy(id, userId);
        if (flag > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean RemoveEnjoyBlog(String id, String userId) {
        QueryWrapper<BlogEnjoy> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", id);
        wrapper.eq("user_id", userId);
        int delete = blogEnjoyMapper.delete(wrapper);
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<EduBlog> enjoyList(String userId) {
        QueryWrapper<BlogEnjoy> wrapper = new QueryWrapper<>();

        wrapper.eq("user_id", userId);
        List<BlogEnjoy> blogEnjoys = blogEnjoyMapper.selectList(wrapper);
        List<String> result = blogEnjoys.stream().map(blogEnjoy -> blogEnjoy.getBlogId()).collect(Collectors.toList());

        List<EduBlog> list = eduBlogMapper.selectBatchIds(result);
        return list;
    }

    @Override
    public PageObject<EduBlog> enjoyList(String userId, Integer page, Integer size) {
        QueryWrapper<BlogEnjoy> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        Page<BlogEnjoy> blogPage = new Page<>(page, size);

        IPage<BlogEnjoy> blogEnjoyIPage = blogEnjoyMapper.selectPage(blogPage, wrapper);
        List<BlogEnjoy> blogEnjoys = blogEnjoyIPage.getRecords();
        List<String> result = blogEnjoys.stream().map(blogEnjoy -> blogEnjoy.getBlogId()).collect(Collectors.toList());
        PageObject<EduBlog> pageObject = new PageObject<>();

        if(result.size()!=0){
            List<EduBlog> list = eduBlogMapper.selectBatchIds(result);
            pageObject.setTotal(blogEnjoyIPage.getTotal());
            pageObject.setResults(list);
        }

        return pageObject;
    }
}

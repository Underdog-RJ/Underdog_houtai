package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.impl.EduBlogServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/eduservice/blog")
public class EduBlogController {


    @Autowired
    private EduBlogService eduBlogService;

    @GetMapping("{id}")
    public R getBlogById(@PathVariable String id){
        EduBlog blog = eduBlogService.getById(id);
        blog.setViewCount(2000);
        eduBlogService.updateById(blog);
        return R.ok().data("blog",blog);
    }

    @PostMapping("pageBlogCondition/{page}/{limit}")
    public R findAllBlog(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false)BlogQuery blogQuery, HttpServletRequest request){
        IPage<EduBlog> iPage=eduBlogService.findByPage(page,limit,blogQuery);
        return R.ok().data("list",iPage.getRecords()).data("total",iPage.getTotal());
    }

    @DeleteMapping("{id}")
    public R deleteBlog(@PathVariable String id){
        boolean flag = eduBlogService.removeById(id);
        if(flag){
            return R.ok().message("删除成功");
        }
        else {
            return R.error().message("删除失败");
        }
    }


    //添加博客
    @PostMapping("addBlogInfo")
    public R addBlogInfo(@RequestBody EduBlog eduBlog,HttpServletRequest request){

        String user = JwtUtils.getUserIdByJwtToken(request);

        eduBlog.setAuthorId(user);
        eduBlog.setPublished(true);
        eduBlog.setRecommend(true);
        eduBlog.setAppreciation(true);
        eduBlog.setShareStatement(true);
        eduBlog.setFlag("Normal");
        eduBlog.setViewCount(1);
        eduBlogService.save(eduBlog);
        return R.ok();
    }
    //修改博客
    @PostMapping("updateBlogInfo")
    public R updateBlogInfo(@RequestBody EduBlog eduBlog){

        boolean flag = eduBlogService.updateById(eduBlog);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }


}


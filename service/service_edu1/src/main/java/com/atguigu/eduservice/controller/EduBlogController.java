package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.vo.BlogQuery;
import com.atguigu.eduservice.mapper.EduBlogMapper;
import com.atguigu.eduservice.service.EduBlogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    @Autowired
    private EduBlogMapper eduBlogMapper;

    @Autowired
    EduSubjectController eduSubjectController;

    @GetMapping("{id}")
    public R getBlogById(@PathVariable String id){
        EduBlog blog = eduBlogService.getById(id);
        blog.setViewCount(2000);
        eduBlogService.updateById(blog);
        return R.ok().data("blog",blog);
    }

    @PostMapping("pageBlogCondition/{page}/{limit}")
    public R findAllBlog(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false)BlogQuery blogQuery, HttpServletRequest request){


        IPage<EduBlog> iPage=eduBlogService.findByPage(page,limit,blogQuery,request);
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


        return eduBlogService.addBlogInfo(eduBlog,request);
    }

    @PostMapping("getBlogByUserId")
    public R getBlogByUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
            String id = userIdByJwtToken.get("id");
            List<EduBlog> list=eduBlogService.getBlogByUserId(id);
            return R.ok().data("list",list);
        }
       return R.ok();
    }

    //根据用户Id获取blog列表
    @PostMapping("findBlogByUserId/{userId}")
    public R findBlogByUserId(@PathVariable String userId){
            List<EduBlog> list=eduBlogService.getBlogByUserId(userId);
            return R.ok().data("list",list);
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

    @GetMapping("test")
    public R test(){

        List<EduBlog> list = eduBlogService.list(null);
        List<String> collect = list.stream().map(EduBlog::getAuthorNickname).collect(Collectors.toList());
        QueryWrapper<EduBlog> wrapper=new QueryWrapper<>();
        Map<String, Object> data = eduSubjectController.getAllSubject().getData();

//        List<EduBlog> java = new LambdaQueryChainWrapper<>(eduBlogMapper).like(EduBlog::getContent, "java").in("").list();

        Map<String,List<EduBlog>> map =list.stream().collect(Collectors.groupingBy(item->item.getSubjectId()));
        System.out.println(map);
        return R.ok();
    }




}


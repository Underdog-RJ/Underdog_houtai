package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.BlogEnjoy;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.service.BlogEnjoyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-15
 */
@RestController
@RequestMapping("/eduservice/blogEnjoy")
public class BlogEnjoyController {

    @Autowired
    public BlogEnjoyService blogEnjoyService;




    //添加收藏
    @GetMapping("enjoyBlog/{id}")
    public R blogEnjoy(@PathVariable String id, HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        boolean flag = blogEnjoyService.addEnjoy(id, userId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }

    //是否已经喜欢
    @GetMapping("IsEnjoyBlog/{id}")
    public R IsEnjoyBlog(@PathVariable String id, HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        boolean flag = blogEnjoyService.isEnjoy(id, userId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }

    //取消喜欢已经喜欢
    @GetMapping("RemoveEnjoyBlog/{id}")
    public R RemoveEnjoyBlog(@PathVariable String id, HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
        boolean flag = blogEnjoyService.RemoveEnjoyBlog(id, userId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }

    }
    //取消喜欢已经喜欢
    @GetMapping("EnjoyBlogList")
    public R EnjoyBlogList( HttpServletRequest request){
        Map<String, String> userIdByJwtToken = JwtUtils.getUserIdByJwtToken(request);
        String userId = userIdByJwtToken.get("id");
      List<EduBlog> list=blogEnjoyService.enjoyList(userId);

      return R.ok().data("list",list);

    }

}


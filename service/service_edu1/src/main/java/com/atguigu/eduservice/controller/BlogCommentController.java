package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.BlogComment;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.mapper.BlogCommentMapper;
import com.atguigu.eduservice.service.BlogCommentService;
import com.atguigu.servicebase.anno.ValidateToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/eduservice/blogcomment")
public class BlogCommentController {

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    BlogCommentMapper blogCommentMapper;

    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id查询评论列表
    @GetMapping("{blogId}/{page}/{limit}")
    public R index(@PathVariable String blogId,
                   @PathVariable Long page,
                   @PathVariable Long limit) {

        Page<BlogComment> pageParam = new Page<>(page, limit);

        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId);
        wrapper.eq("parent_id", "");
        wrapper.orderByDesc("gmt_create");
        blogCommentService.page(pageParam, wrapper);
        // 父级
        List<BlogComment> EduCommentList = pageParam.getRecords();
        // 封装子集
        for (BlogComment blogComment : EduCommentList) {
            QueryWrapper<BlogComment> sonWarpper = new QueryWrapper<>();
            String parentId = blogComment.getId();
            sonWarpper.eq("parent_id", parentId);
            sonWarpper.last("limit 2");
            List<BlogComment> blogComments = blogCommentMapper.selectList(sonWarpper);
            blogComment.setChildList(blogComments);
            // 查询子集数量
            Integer integer = blogCommentMapper.selectCount(new QueryWrapper<BlogComment>().lambda().eq(BlogComment::getParentId, parentId));
            blogComment.setSonTotal(integer);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("items", EduCommentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    @ValidateToken
    public R save(@RequestBody BlogComment comment, HttpServletRequest request) {
        return blogCommentService.addCommit(comment, request);
    }


    @PostMapping("commentChild/{blogId}/{page}/{size}")
    public R commentChild(@PathVariable String blogId, @PathVariable Integer page, @PathVariable Integer size) {
        return blogCommentService.commentChild(blogId, page, size);
    }


}


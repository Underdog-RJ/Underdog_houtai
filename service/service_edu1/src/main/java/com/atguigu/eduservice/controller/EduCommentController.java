package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.BlogComment;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;

import com.atguigu.servicebase.anno.ValidateToken;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-31
 */
@RestController
@RequestMapping("/eduservice/comment")
public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduCommentMapper eduCommentMapper;


    //根据课程id查询评论列表
    @GetMapping("{courseId}/{page}/{limit}")
    public R index(  @PathVariable String courseId,
                     @PathVariable Long page,
                     @PathVariable  Long limit){

        Page<EduComment> pageParam = new Page<>(page, limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.orderByDesc("gmt_create");
        wrapper.eq("parent_id", "");

        eduCommentService.page(pageParam,wrapper);
        List<EduComment> EduCommentList = pageParam.getRecords();
        // 封装子集
        for (EduComment eduComment : EduCommentList) {
            QueryWrapper<EduComment> sonWarpper = new QueryWrapper<>();
            String parentId = eduComment.getId();
            sonWarpper.eq("parent_id", parentId);
            sonWarpper.last("limit 2");
            List<EduComment> blogComments = eduCommentMapper.selectList(sonWarpper);
            eduComment.setChildList(blogComments);
            // 查询子集数量
            Integer integer = eduCommentMapper.selectCount(new QueryWrapper<EduComment>().lambda().eq(EduComment::getParentId, parentId));
            eduComment.setSonTotal(integer);
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
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        return eduCommentService.addCommit(comment,request);
    }

    @PostMapping("commentChild/{courseId}/{page}/{size}")
    public R commentChild(@PathVariable String courseId, @PathVariable Integer page, @PathVariable Integer size) {
        return eduCommentService.commentChild(courseId, page, size);
    }

    @DeleteMapping("deleteComment/{commentId}")
    @ValidateToken
    public R deleteComment(@PathVariable String commentId){
        return eduCommentService.deleteComment(commentId);
    }

}


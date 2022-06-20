package com.atguigu.eduservice.controller;

import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduBlog;
import com.atguigu.eduservice.entity.EduBlogTop;
import com.atguigu.eduservice.entity.UcenterMemberPay;
import com.atguigu.eduservice.service.EduBlogService;
import com.atguigu.eduservice.service.EduBlogTopService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 每周统计前8，送Ub
 */
@RestController
@RequestMapping("/task/cron")
public class SchedulTaskController {


    @Autowired
    private EduBlogService eduBlogService;

    @Autowired
    private EduBlogTopService eduBlogTopService;

    @Autowired
    private UcenterClient ucenterClient;

    //每周星期天凌晨1点执行一次
    @Scheduled(cron = "0 0 0 ? * MON")
    public void task(){

        long end = System.currentTimeMillis();
        long  start=end-604800000;
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatStart = sdf.format(new Date(start));
        String formatEnd = sdf.format(new Date(end));

        // 按浏览量排序，取前8条
        QueryWrapper<EduBlog> wrapper=new QueryWrapper<>();
        wrapper.between("gmt_create",formatStart,formatEnd);
        wrapper.eq("type",1);
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduBlog> list = eduBlogService.list(wrapper);

        for (int i = 0; i < list.size(); i++) {
            //第一名+100
            if(i==0){
                EduBlog eduBlog = list.get(i);
                String userId = eduBlog.getAuthorId();
                UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(userId);
                Integer uCoin = ucenterPay.getUCoin();
                ucenterClient.updateUseruCoinById(uCoin+100,userId);
            }
            else if(i==1){
                EduBlog eduBlog = list.get(i);
                String userId = eduBlog.getAuthorId();
                UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(userId);
                Integer uCoin = ucenterPay.getUCoin();
                ucenterClient.updateUseruCoinById(uCoin+80,userId);
            }
            else if(i==2){
                EduBlog eduBlog = list.get(i);
                String userId = eduBlog.getAuthorId();
                UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(userId);
                Integer uCoin = ucenterPay.getUCoin();
                ucenterClient.updateUseruCoinById(uCoin+60,userId);
            }else {
                EduBlog eduBlog = list.get(i);
                String userId = eduBlog.getAuthorId();
                UcenterMemberPay ucenterPay = ucenterClient.getUcenterPay(userId);
                Integer uCoin = ucenterPay.getUCoin();
                ucenterClient.updateUseruCoinById(uCoin+50,userId);
            }
        }
        for (EduBlog eduBlog : list) {
            eduBlog.setGmtCreate(null);
            eduBlog.setGmtModified(null);
            EduBlogTop eduBlogTop=new EduBlogTop();
            BeanUtils.copyProperties(eduBlog,eduBlogTop);
            eduBlogTopService.save(eduBlogTop);
        }


    }

}

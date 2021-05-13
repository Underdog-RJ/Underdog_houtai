package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.order.CourseWebVoOrder;
import com.atguigu.commonutils.order.UcenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.vo.OrderQuery;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String createOrders(String courseId, String memberId) {
        //远程调用根据用户id获取用户信息
        UcenterMemberOrder ucenterMember = ucenterClient.getUserInfoOrder(memberId);
        //远程调用根据课程id获取课程信息
        CourseWebVoOrder courseDto = eduClient.getCourseInfoOrder(courseId);

        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());

        order.setStatus(0); //订单状态
        order.setPayType(1); //订单类型

        baseMapper.insert(order);

        return order.getOrderNo();
    }

    @Override
    public IPage<Order> findAll(Long page, Long limit, OrderQuery orderQuery) {
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        String teacherName = orderQuery.getTeacherName();
        String courseTitle = orderQuery.getCourseTitle();
        String begin = orderQuery.getBegin();
        String end = orderQuery.getEnd();
        Integer status = orderQuery.getStatus();
        Integer payType = orderQuery.getPayType();
        if(!StringUtils.isEmpty(payType)){
            wrapper.eq("pay_type",payType);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(teacherName)){
            wrapper.like("teacher_name",teacherName);
        }
        if(!StringUtils.isEmpty(courseTitle)){
            wrapper.like("course_title",courseTitle);
        }
        if(!StringUtils.isEmpty(begin))
        {
            //构造模糊查询条件
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            //构造模糊查询条件
            wrapper.le("gmt_modified",end);
        }
        Page<Order> ipage=new Page<>(page,limit);

        return orderMapper.selectPage(ipage, wrapper);

    }
}

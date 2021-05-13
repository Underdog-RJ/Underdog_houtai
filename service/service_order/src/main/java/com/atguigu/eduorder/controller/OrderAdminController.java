package com.atguigu.eduorder.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.vo.OrderQuery;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduorder/admin")
public class OrderAdminController {


    @Autowired
    private OrderService orderService;


    @PostMapping("/all/{page}/{limit}")
    public R getAllOrder(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) OrderQuery orderQuery){
        IPage<Order> orderIPage= orderService.findAll(page,limit,orderQuery);
        List<Order> list = orderIPage.getRecords();
        long total = orderIPage.getTotal();
        return R.ok().data("list",list).data("total",total);
    }

}

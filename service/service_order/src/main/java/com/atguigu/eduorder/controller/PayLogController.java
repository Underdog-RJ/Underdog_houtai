package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @PostMapping("toPay")
    private R toPay(HttpServletRequest request,@RequestBody Map map){

        return  payLogService.toPay(request,map);
    }

    //生成微信值父二维码接口
    //参数式订单号
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo)
    {
        //返回信息，包含二维码地址，还有其他需要的信息
        Map map=payLogService.createNative(orderNo);
        System.out.println("返回二维码map集合："+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    //参数：订单号，根据订单号查询，支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //调用查询接口
        boolean flag= payLogService.queryPayStatus(orderNo);
        if (flag==false) {//出错
            return R.error().message("支付出错");
        }
        if (flag) {//如果成功
            //更改订单状态
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }
}


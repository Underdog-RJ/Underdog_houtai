package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EduClient eduClient;



    //生成微信二维码
    @Override
    public Map createNative(String orderNo) {
        try {
            //根据订单号查询订单信息
            QueryWrapper<Order> wrapper =new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191"); //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//生成随机的字符串
            m.put("body", order.getCourseTitle());//课程标题
            m.put("out_trade_no", orderNo);//订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//价格
            m.put("spbill_create_ip", "127.0.0.1");//支付地址
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //发送httpclien请求，传递参数xml格式，微信支付提供固定的地址
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //设置xml格式的参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));//商户key
            client.setHttps(true);//支持https

            //执行请求发送
            client.post();

            //得到发送请求返回i结果，返回结果的形式上xml
            String xml = client.getContent();

            //把xml格式转换为map集合把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //返回数据的封装
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));//返回二维码操作状态码
            map.put("code_url", resultMap.get("code_url"));//二维码地址
            return map;

        } catch (Exception e) {
            throw new GuliException(20001,"生成二维码失败");
        }


    }

    //查询订单字符状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2发送httpclient
            HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            
            //得到请求返回内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //转换map在返回
            return resultMap;
        } catch (Exception e) {
            return null;
        }

    }

    //添加支付记录和更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单号
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);
        //更行订单表的订单状态
        if(order.getStatus().intValue()==1) {return;}
        order.setStatus(1);//1代表已支付
        eduClient.updateBuyCount( order.getCourseId());
        orderService.updateById(order);

        //向支付附表中添加支付记录
        PayLog payLog=new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));//流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }
}

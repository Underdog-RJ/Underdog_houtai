package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.vo.OrderQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String memberIdByJwtToken);

    IPage<Order> findAll(Long page, Long limit, OrderQuery orderQuery);
}

package com.atguigu.eduorder.service;

import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-02-01
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);

    boolean queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);

    R toPay(HttpServletRequest request, Map map);
}

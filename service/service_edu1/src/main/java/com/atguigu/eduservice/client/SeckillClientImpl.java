package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;

/**
 * @author underdog_rj
 * @date2022/4/2711:49
 */
public class SeckillClientImpl implements SeckillClient{
    @Override
    public R getSkuSeckillInfo(String id) {
        return R.ok().data("data","");
    }
}

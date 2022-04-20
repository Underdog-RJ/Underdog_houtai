package com.atguigu.pierce.service;

import com.atguigu.commonutils.R;
import com.atguigu.pierce.entity.Client;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface PierceService extends IService<Client> {
    R hasPort();


    R createTunnel(Client client, HttpServletRequest httpServletRequest);

    R getUserChannel(HttpServletRequest request, Integer page, Integer size);

    R changeClientKey(Client client);

    R getUserMetrics(Integer page, Integer size, HttpServletRequest request);

    R updateClient(Client client);
}

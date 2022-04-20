package com.atguigu.pierce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ClientMQ {

    /**
     * 代理客户端唯一标识key
     */
    private String clientKeyBefore;
    /**
     * 代理客户端唯一标识key
     */
    private String clientKeyNow;

    /**
     * 代理服务器端口
     */
    private Integer inetPort;

    /**
     * 需要代理的网络信息（代理客户端能够访问），格式 192.168.1.99:80 (必须带端口)
     */
    private String lanBefore;

    private String lanNow;

    private Integer type;
}

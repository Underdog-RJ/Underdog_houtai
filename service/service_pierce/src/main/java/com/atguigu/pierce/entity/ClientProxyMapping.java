package com.atguigu.pierce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 代理客户端的真实信息
 */
@Data
public class ClientProxyMapping {

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /** 代理服务器端口 */
    private Integer inetPort;

    /** 需要代理的网络信息（代理客户端能够访问），格式 192.168.1.99:80 (必须带端口) */
    private String lan;

    /** 备注名称 */
    private String name;
}

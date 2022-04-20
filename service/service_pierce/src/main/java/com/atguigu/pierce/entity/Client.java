package com.atguigu.pierce.entity;

import com.atguigu.pierce.netty.config.ProxyConfig;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 代理客户端
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("client")
public class Client {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 客户端备注名称
     */
    private String name;

    /**
     * 代理客户端唯一标识key
     */
    private String clientKey;

    /** 代理服务器端口 */
    private Integer inetPort;

    /** 需要代理的网络信息（代理客户端能够访问），格式 192.168.1.99:80 (必须带端口) */
    private String lan;

    private int status;

    private String userId;

    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}

package com.atguigu.eduservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFriendReq对象", description="")
public class TbFriendReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "请求好友用户id")
    private String fromUserid;

    @ApiModelProperty(value = "被请求好友用户id")
    private String toUserid;

    @ApiModelProperty(value = "请求时间")
    private Date createtime;

    @ApiModelProperty(value = "发送的消息")
    private String message;

    @ApiModelProperty(value = "是否已处理")
    private Integer status;


}

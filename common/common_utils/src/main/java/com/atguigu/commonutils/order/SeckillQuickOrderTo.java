package com.atguigu.commonutils.order;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillQuickOrderTo
{

    private String orderSn;

    private Integer promotionSessionId;

    private String skuId;

    private BigDecimal seckillPrice;

    private Integer num;

    private String memberId;

    private String username;

}

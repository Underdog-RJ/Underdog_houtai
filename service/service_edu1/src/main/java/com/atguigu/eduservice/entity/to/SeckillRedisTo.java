package com.atguigu.eduservice.entity.to;


import com.atguigu.eduservice.entity.SkuInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class SeckillRedisTo {


  private Integer promotionId;


  private Integer promotionSessionId;


  private String skuId;

  private String createTime;


  private BigDecimal seckillPrice;


  private Integer seckillCount;

  private Integer seckillLimit;

  private Integer seckillSort;

  private SkuInfoVo skuInfoVo;

  @ApiModelProperty("当前商品的开始时间")
  private Long startTime;

  @ApiModelProperty("当前商品的结束时间")
  private Long endTime;

  @ApiModelProperty("当前商品的随机码")
  private String randomCode;
}

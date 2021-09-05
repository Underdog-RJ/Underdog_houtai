package com.atguigu.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "seckill_sku_relation")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeckillSkuRelation implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "promotion_id")
  private Integer promotionId;

  @Column(name = "promotion_session_id")
  private Integer promotionSessionId;

  @Column(name = "sku_id")
  private String skuId;

  @Column(name = "create_time")
  private String createTime;


  @Column(name = "seckill_price")
  private BigDecimal seckillPrice;

  @Column(name = "seckill_count")
  private Integer seckillCount;

  @Column(name = "seckill_limit")
  private Integer seckillLimit;


  @Column(name = "seckill_sort")
  private Integer seckillSort;



}

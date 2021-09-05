package com.atguigu.entity;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="seckill_session")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SeckillSession implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "start_time")
  private String startTime;

  @Column(name = "end_time")
  private String endTime;

  @Column(name = "status")
  private Integer status;

  @Column(name = "create_time")
  private String createTime;

  @Transient
  private List<SeckillSkuRelation> relationSkus;


}

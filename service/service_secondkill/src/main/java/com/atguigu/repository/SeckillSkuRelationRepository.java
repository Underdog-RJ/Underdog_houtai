package com.atguigu.repository;

import com.atguigu.entity.SeckillSkuRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeckillSkuRelationRepository extends JpaRepository<SeckillSkuRelation,Integer> {

  List<SeckillSkuRelation> findByPromotionSessionId(Integer integer);
}

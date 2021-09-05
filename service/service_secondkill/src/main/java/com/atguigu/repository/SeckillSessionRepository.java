package com.atguigu.repository;

import com.atguigu.entity.SeckillSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeckillSessionRepository extends JpaRepository<SeckillSession,Integer> {

  List<SeckillSession> findSeckillSessionByStartTimeBetween(String startTime, String endTime);

}

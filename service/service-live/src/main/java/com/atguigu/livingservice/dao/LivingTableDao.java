package com.atguigu.livingservice.dao;

import com.atguigu.livingservice.entity.MessageType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LivingTableDao extends MongoRepository<MessageType,String> {


    List<MessageType> findByLivingIdOrderByDate(String id);

}

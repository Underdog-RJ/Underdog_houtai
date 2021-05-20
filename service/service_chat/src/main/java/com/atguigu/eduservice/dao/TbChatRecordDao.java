package com.atguigu.eduservice.dao;

import com.atguigu.eduservice.entity.TbChatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TbChatRecordDao extends MongoRepository<TbChatRecord,String> {
}

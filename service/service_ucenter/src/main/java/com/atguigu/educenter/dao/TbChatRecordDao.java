package com.atguigu.educenter.dao;


import com.atguigu.educenter.entity.TbChatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbChatRecordDao extends MongoRepository<TbChatRecord,String> {

    List<TbChatRecord> findByUserIdAndFriendIdAndHasDeleteOrderByCreatetime(String userId,String friendId,Integer number);

    List<TbChatRecord> findByFriendIdAndHasRead(String friendId,Integer integer);


}

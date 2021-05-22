package com.atguigu.eduservice.service;


import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.entity.TbChatRecordVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ChatRecordService {
    void insert(TbChatRecord chatRecord);

    List<TbChatRecordVo> findByUserIdAndFriendId(String userid, String friendid);

    List<TbChatRecord> getRecordByUserId(String userId);

    void updateStatusHasRead(String id);
}

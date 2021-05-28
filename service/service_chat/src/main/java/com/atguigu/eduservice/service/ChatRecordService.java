package com.atguigu.eduservice.service;


import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.entity.TbChatRecordVo;
import com.atguigu.eduservice.entity.UnReadMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ChatRecordService {
    void insert(TbChatRecord chatRecord);

    List<TbChatRecordVo> findByUserIdAndFriendId(String userid, String friendid);

    List<UnReadMessage> getRecordByUserId(String userId);

    void updateStatusHasRead(String id);
}

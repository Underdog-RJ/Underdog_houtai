package com.atguigu.educenter.service;



import com.atguigu.educenter.entity.TbChatRecord;
import com.atguigu.educenter.entity.TbChatRecordVo;
import com.atguigu.educenter.entity.UnReadMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ChatRecordService {
    void insert(TbChatRecord chatRecord);

    List<TbChatRecordVo> findByUserIdAndFriendId(String userid, String friendid);

    List<UnReadMessage> getRecordByUserId(String userId);

    void updateStatusHasRead(String id);

    List<TbChatRecord> test();

    void testDelete();
}

package com.atguigu.eduservice.service;


import com.atguigu.eduservice.entity.TbChatRecord;
import org.springframework.stereotype.Service;


public interface ChatRecordService {
    void insert(TbChatRecord chatRecord);
}

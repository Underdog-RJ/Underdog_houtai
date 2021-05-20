package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.dao.TbChatRecordDao;
import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.service.ChatRecordService;
import com.atguigu.eduservice.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbChatRecordDao tbChatRecordDao;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:MM");



    /**
     * 将聊天记录保存到数据库中
     * @param chatRecord
     */
    @Override
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setHasRead(0);
        chatRecord.setCreatetime(sdf.format(new Date()));
        chatRecord.setHasDelete(0);
        TbChatRecord save = tbChatRecordDao.save(chatRecord);
        System.out.println(save);
    }
}

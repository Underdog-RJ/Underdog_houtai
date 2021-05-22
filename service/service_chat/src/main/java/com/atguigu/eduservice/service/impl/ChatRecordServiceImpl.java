package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.dao.TbChatRecordDao;
import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.entity.TbChatRecordVo;
import com.atguigu.eduservice.service.ChatRecordService;
import com.atguigu.eduservice.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbChatRecordDao tbChatRecordDao;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



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


    @Override
    public List<TbChatRecordVo> findByUserIdAndFriendId(String userid, String friendid) {
        List<TbChatRecordVo> findList=new ArrayList<>();
        List<TbChatRecord> userToFriend = tbChatRecordDao.findByUserIdAndFriendIdAndHasDeleteOrderByCreatetime(userid, friendid,0);
        for (TbChatRecord tbChatRecord : userToFriend) {
            findList.add(new TbChatRecordVo(1,tbChatRecord.getMessage(),tbChatRecord.getCreatetime()));
        }
        List<TbChatRecord> friendToUser = tbChatRecordDao.findByUserIdAndFriendIdAndHasDeleteOrderByCreatetime(friendid,userid, 0);
        /**
         * 更新或有聊天状态为已读
         */
        for (TbChatRecord tbChatRecord : friendToUser) {
            if(!tbChatRecord.getHasRead().equals(1)){
                tbChatRecord.setHasRead(1);
                tbChatRecordDao.save(tbChatRecord);
            }
        }
        for (TbChatRecord tbChatRecord : friendToUser) {
            findList.add(new TbChatRecordVo(2,tbChatRecord.getMessage(),tbChatRecord.getCreatetime()));
        }
        Collections.sort(findList);

        return findList;
    }

    @Override
    public List<TbChatRecord> getRecordByUserId(String userId) {

        List<TbChatRecord> list = tbChatRecordDao.findByUserIdAndHasRead(userId, 1);

        return list;
    }

    /**
     * 设置消息为已读
     * @param id
     */
    @Override
    public void updateStatusHasRead(String id) {

        Optional<TbChatRecord> optional = tbChatRecordDao.findById(id);
        if(optional.isPresent()){
            TbChatRecord tbChatRecord = optional.get();
            tbChatRecord.setHasRead(1);
            tbChatRecordDao.save(tbChatRecord);
        }


    }
}

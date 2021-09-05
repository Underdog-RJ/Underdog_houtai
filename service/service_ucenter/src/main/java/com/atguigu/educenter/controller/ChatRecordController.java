package com.atguigu.educenter.controller;


import com.atguigu.commonutils.R;

import com.atguigu.educenter.dao.TbChatRecordDao;
import com.atguigu.educenter.entity.TbChatRecord;
import com.atguigu.educenter.entity.TbChatRecordVo;
import com.atguigu.educenter.entity.UnReadMessage;
import com.atguigu.educenter.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/educenter/chat/chatRecord")
public class ChatRecordController {


    @Autowired
    private ChatRecordService chatRecordService;

    @Autowired
    private TbChatRecordDao tbChatRecordDao;

    @Autowired
    MongoTemplate mongoTemplate;

    //获取当前用于与朋友的聊天记录
    @GetMapping("findByUserIdAndFriendId/{userId}/{friendId}")
    public R findByUserIdAndFriendId(@PathVariable String userId,@PathVariable String friendId){
        List<TbChatRecordVo> list=chatRecordService.findByUserIdAndFriendId(userId,friendId);
        return R.ok().data("list",list);
    }


    /**
     * 根据用户id获取未读消息
     * @param userId
     * @return
     */
    @GetMapping("getRecordByUserId/{userId}")
    public R getRecordByUserId(@PathVariable String userId){
        List<UnReadMessage> list=chatRecordService.getRecordByUserId(userId);
        return R.ok().data("list",list);
    }

    @GetMapping("/test")
    public R test(){

        List<TbChatRecord> list =chatRecordService.test();

        Query query=new Query();
        Criteria criteria = new Criteria();
//        criteria.and("message").is("32");
//        query.addCriteria(criteria);
        List<TbChatRecord> collect1 = tbChatRecordDao.findAll().stream().collect(Collectors.toList());

        Pageable pageable= PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createTime")));
//        Sort.Order desc = Sort.Order.desc();
        query.with(pageable);
        List<TbChatRecord> collect = mongoTemplate.find(query, TbChatRecord.class).stream().collect(Collectors.toList());


        return null;

    }
    @GetMapping("/testdelete")
    public R testDelete(){
        chatRecordService.testDelete();
        return null;
    }

}

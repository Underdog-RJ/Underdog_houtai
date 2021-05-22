package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.TbChatRecord;
import com.atguigu.eduservice.entity.TbChatRecordVo;
import com.atguigu.eduservice.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/chat/chatRecord")
public class ChatRecordController {


    @Autowired
    private ChatRecordService chatRecordService;

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

        List<TbChatRecord> list=chatRecordService.getRecordByUserId(userId);


        return R.ok().data("list",list);



    }

}
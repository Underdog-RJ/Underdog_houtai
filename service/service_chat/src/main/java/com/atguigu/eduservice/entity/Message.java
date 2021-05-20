package com.atguigu.eduservice.entity;

import lombok.Data;

@Data
public class Message {
    private String type; //消息类型
    private TbChatRecord tbChatRecord; //聊天消息
    private Object ext; //扩展消息字段
}

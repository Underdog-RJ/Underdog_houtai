package com.atguigu.eduservice.entity;

import lombok.Data;

@Data
public class TbChatRecord {

    private String id;
    private String userId;
    private String friendId;
    private String msg;
}

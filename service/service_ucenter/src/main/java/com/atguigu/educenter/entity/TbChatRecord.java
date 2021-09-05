package com.atguigu.educenter.entity;

import lombok.Data;

@Data
public class TbChatRecord {

    private String id;
    private String userId;
    private String friendId;
    private Integer hasRead;
    private String createtime;
    private Integer hasDelete;
    private String message;




}

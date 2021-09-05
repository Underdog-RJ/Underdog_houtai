package com.atguigu.eduservice.entity;

import lombok.Data;

@Data
public class MessageType {
    private String livingId;
    private String userId;
    private Integer type;
    private String message;
    private String date;
    private String userAvatar;
    private Integer count;
}

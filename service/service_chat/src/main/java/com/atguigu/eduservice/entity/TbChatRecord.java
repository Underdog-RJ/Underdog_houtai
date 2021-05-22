package com.atguigu.eduservice.entity;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

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

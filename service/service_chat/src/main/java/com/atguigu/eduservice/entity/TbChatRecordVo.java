package com.atguigu.eduservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TbChatRecordVo implements Comparable<TbChatRecordVo> {


    private int type;

    private String msg;

    private String createtiem;

    @Override
    public int compareTo(TbChatRecordVo o) {
        return this.createtiem.compareTo(o.getCreatetiem());
    }
}

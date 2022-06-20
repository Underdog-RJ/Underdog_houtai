package com.atguigu.vod.entity;

import lombok.Data;

@Data
public class QueryMediaFileRequest {

    private String fileOriginalName;
    private String processStatus;
    private String fileType;

}

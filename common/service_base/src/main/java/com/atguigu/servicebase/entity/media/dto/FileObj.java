package com.atguigu.servicebase.entity.media.dto;

import lombok.Data;

@Data
public class FileObj {
    String fileMd5;
    String fileName;
    Long fileSize;
    String mimetype;
    String fileExt;
    Integer chunk;
    Integer chunkSize;
}

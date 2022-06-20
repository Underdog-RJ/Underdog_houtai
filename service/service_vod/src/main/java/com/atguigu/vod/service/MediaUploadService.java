package com.atguigu.vod.service;

import com.atguigu.commonutils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface MediaUploadService {
    R register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    R checkchunk(String fileMd5, Integer chunk, Integer chunkSize);

    R uploadchunk(MultipartFile file, Integer chunk, String fileMd5);

    R mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt, HttpServletRequest request);
}

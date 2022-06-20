package com.atguigu.vod.controller;

import com.atguigu.commonutils.R;
import com.atguigu.servicebase.anno.ValidateToken;
import com.atguigu.servicebase.entity.media.dto.FileObj;
import com.atguigu.vod.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/eduvod/media/upload")
public class MediaUploadController {

    @Autowired
    MediaUploadService mediaUploadService;

    //文件上传前的校验

    @PostMapping("/register")
    public R register(@RequestBody FileObj fileObj) {
        return mediaUploadService.register(fileObj.getFileMd5(), fileObj.getFileName(), fileObj.getFileSize(), fileObj.getMimetype(), fileObj.getFileExt());
    }


    @PostMapping("/checkchunk")
    public R checkchunk(@RequestBody FileObj fileObj) {
        return mediaUploadService.checkchunk(fileObj.getFileMd5(), fileObj.getChunk(), fileObj.getChunkSize());
    }


    @PostMapping("/uploadchunk")
    public R uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        return mediaUploadService.uploadchunk(file, chunk, fileMd5);
    }


    @PostMapping("/mergechunks")
    @ValidateToken
    public R mergechunks(@RequestBody FileObj fileObj, HttpServletRequest request) {

        return mediaUploadService.mergechunks(fileObj.getFileMd5(), fileObj.getFileName(), fileObj.getFileSize(), fileObj.getMimetype(), fileObj.getFileExt(),request);
    }
}

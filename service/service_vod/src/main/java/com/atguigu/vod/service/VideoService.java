package com.atguigu.vod.service;

import com.atguigu.commonutils.R;
import com.atguigu.vod.entity.QueryMediaFileRequest;

import javax.servlet.http.HttpServletRequest;

public interface VideoService {
    R findVideo(HttpServletRequest request, Integer page, Integer size, QueryMediaFileRequest queryMediaFileRequest);

}

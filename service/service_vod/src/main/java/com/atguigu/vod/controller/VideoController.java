package com.atguigu.vod.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicebase.anno.ValidateToken;
import com.atguigu.vod.entity.QueryMediaFileRequest;
import com.atguigu.vod.service.VideoService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/eduvod/media/upload")
public class VideoController {

    @Autowired
    VideoService videoService;

    @PostMapping("findVideo/{page}/{size}")
    @ValidateToken
    public R findVideo(HttpServletRequest request, @PathVariable Integer page, @PathVariable Integer size, @RequestBody QueryMediaFileRequest queryMediaFileRequest) {
       return  videoService.findVideo(request,page,size,queryMediaFileRequest);
    }

}

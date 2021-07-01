package com.atguigu.livingservice.client;


import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(value = "service-vod")
public interface VodClient {


    //上传视频到阿里云
    @PostMapping(value = "/eduvod/video/uploadAlyiVideo",consumes = "multipart/form-data")
    R uploadAlyiVideo(MultipartFile file, @RequestHeader(name = "token", required = true) String token);

    //根据视频id删除阿里云视频  必须写完全路径  @PathVariable 必须只能参数名称
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);


    //根据视频id获取视频凭证
    @GetMapping("/eduvod/video/getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable("id") String id);
}

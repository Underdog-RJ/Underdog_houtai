package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient{

    // 出错之后执行的方法
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBantch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");

    }

    @Override
    public R uploadAlyiVideo(MultipartFile file, String token) {
        return null;
    }

    @Override
    public R getPlayAuth(String id) {
        return null;
    }
}

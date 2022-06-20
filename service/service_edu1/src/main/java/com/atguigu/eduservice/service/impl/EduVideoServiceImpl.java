package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-01-21
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private EduVideoMapper eduVideoMapper;

    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideoByCourseId(String courseId) {
        //1 根据课程id查询课程所有的视频id
        QueryWrapper wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.select("video_source_id");//查询指定列
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        List<String> videoIds = new ArrayList<>();
        for (EduVideo eduVideo : eduVideoList) {
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())) {
                videoIds.add(eduVideo.getVideoSourceId());
            }
        }
        //远程调用删除多个视频
        if (videoIds.size() > 0) {
            vodClient.deleteBantch(videoIds);
        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }

    @Override
    public R addVideoUrl(EduVideo eduVideo) {
        EduVideo result = eduVideoMapper.selectById(eduVideo.getId());
        result.setVideoSourceId(eduVideo.getVideoSourceId());
        result.setVideoOriginalName(eduVideo.getVideoOriginalName());
        result.setVideoUrl(eduVideo.getVideoUrl());
        eduVideoMapper.updateById(result);
        return R.ok();
    }
}

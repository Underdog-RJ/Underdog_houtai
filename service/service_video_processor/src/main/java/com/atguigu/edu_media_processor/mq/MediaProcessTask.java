package com.atguigu.edu_media_processor.mq;


import com.alibaba.fastjson.JSON;
import com.atguigu.edu_media_processor.dao.MediaFileRepository;
import com.atguigu.edu_media_processor.utils.HlsVideoUtil;
import com.atguigu.edu_media_processor.utils.Mp4VideoUtil;
import com.atguigu.servicebase.entity.media.MediaFile;
import com.atguigu.servicebase.entity.media.MediaFileProcess_m3u8;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RabbitListener(queues = "${underdog-service-manage-media.mq.queue-media-video-processor}", containerFactory = "customContainerFactory")
public class MediaProcessTask {

    @Autowired
    MediaFileRepository mediaFileRepository;

    @Value("${underdog-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;
    @Value("${underdog-service-manage-media.video-location}")
    String serverPath;

    //接收视频处理消息进行视频处理
    @RabbitHandler
    public void receiveMediaProcessTask(String msg) {
        //1.解析消息内容，得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        //2.拿media从数据库查询文件信息
        String mediaId = (String) map.get("mediaId");
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent())
            return;
        MediaFile mediaFile = optional.get();
        String fileType = mediaFile.getFileType();
        String mp4_name = "";
        if (fileType.equals("mp4")) {
            mediaFile.setProcessStatus("303001"); // 无需处理
            mediaFileRepository.save(mediaFile);
            mp4_name = mediaFile.getFileId() + ".mp4";
        } else if (fileType.equals("avi")) {
            // 需要处理
            mediaFile.setProcessStatus("303001"); // 处理中
            mediaFileRepository.save(mediaFile);
            // 3.使用工具类将avi转换成mp4
            // 要处理的文件路径
            String video_path = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
            // 生成的mp4的文件名称
            mp4_name = mediaFile.getFileId() + ".mp4";
            // 生成的mp4所在路径
            String mp4_folder_path = serverPath + mediaFile.getFilePath();
            // 创建工具类
            Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4_folder_path);

            String result = mp4VideoUtil.generateMp4();

            if (StringUtils.isEmpty(result) || !result.equals("success")) {
                // 处理失败
                mediaFile.setProcessStatus("303003");
                // 定义mediaFileProcess_m3u8
                MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
                mediaFileProcess_m3u8.setErrormsg(result);
                mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);////记录失败原因
                mediaFileRepository.save(mediaFile);
                return;
            }
        }

        // 4.将mp4生成m3u8和ts文件
        //mp4视频文件路径
        String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4_name;
        //m3u8名称
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        //m3u8文件所在目录
        String m3u8folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path, mp4_video_path, m3u8_name, m3u8folder_path);
        //生成m3u8和ts文件
        String teResult = hlsVideoUtil.generateM3u8();
        if (teResult == null || !teResult.equals("success")) {
            // 处理失败
            mediaFile.setProcessStatus("303003");
            //定义mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(teResult);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8); //记录失败原因
            mediaFileRepository.save(mediaFile);
            return;
        }
        // 处理成功
        // 获取ts列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

        // 保存fileUrl (此url就是视频播放的相对路径
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8_name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileRepository.save(mediaFile);

    }


}

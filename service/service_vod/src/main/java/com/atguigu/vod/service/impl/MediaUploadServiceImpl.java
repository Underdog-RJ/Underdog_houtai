package com.atguigu.vod.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.vod.config.RabbitMQConfig;
import com.atguigu.vod.dao.MediaFileRepository;
import com.atguigu.servicebase.entity.media.MediaFile;
import com.atguigu.vod.service.MediaUploadService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@Service
public class MediaUploadServiceImpl implements MediaUploadService {

    @Autowired
    MediaFileRepository mediaFileRepository;
    @Value("${underdog-service-manage-media.upload-location}")
    String upload_location;

    //视频处理路由
    @Value("${underdog-service-manage-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    @Autowired
    RabbitTemplate rabbitTemplate;

    // 得到文件所属的目录路径
    private String getFileFoldPath(String fileMd5) {
        return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return this.getFileFoldPath(fileMd5) + fileMd5 + "." + fileExt;
    }

    //得到块文件所属的目录路径
    private String getChunkFileFoldPath(String fileMd5) {
        return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "/chunk/";
    }


    /**
     * 根据md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     *
     * @param fileMd5  文件md5值
     * @param fileName 文件扩展名
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    @Override
    public R register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 1.检查文件在磁盘上是否存在
        // 文件所属的路径
        String fileFoldPath = this.getFileFoldPath(fileMd5);
        // 文件路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        // 文件是否存在
        File file = new File(filePath);
        boolean exists = file.exists();
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if (exists && optional.isPresent()) {
            R.ok().code(20030).message("文件已经存在");
        }
        // 文件不存在时做一些准备工作，检查文件所在的目录是否存在，如果不存在则创建
        File fileFolder = new File(fileFoldPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return R.ok().message("文件注册成功");
    }

    // 分块检查

    /**
     * @param fileMd5   md
     * @param chunk     块的下标
     * @param chunkSize // 块的大小
     * @return
     */
    @Override
    public R checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 检查分块文件是否存在
        // 得到分块文件所在的目录
        String chunkFileFoldPath = this.getChunkFileFoldPath(fileMd5);
        File chunkFile = new File(chunkFileFoldPath + chunk);
        if (chunkFile.exists()) {
            // 块为文件存在
            return R.ok().data("flag", true);
        } else {
            return R.ok().data("flag", false);
        }
    }

    // 上传分块
    @Override
    public R uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        // 分叉分块目录，如果不存在则自动创建
        String chunkFileFoldPath = this.getChunkFileFoldPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFoldPath);
        // 不存在创建
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }
        // 得到上传文件的输入流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(chunkFileFoldPath + chunk));
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            //流拷贝
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok().success(true);
    }

    // 合并文件
    @Override
    public R mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        // 1.合并所有的分块
        String chunkFileFoldPath = this.getChunkFileFoldPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFoldPath);
        // 文件分块文件列表
        File[] files = chunkFileFolder.listFiles();
        List<File> fileList = Arrays.asList(files);

        // 创建一个合并文件
        String filePath = this.getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);


        // 执行合并
        mergeFile = this.mergeFile(fileList, mergeFile);
        if (mergeFile == null) {
            R.ok().success(false);
        }
        // 2.检验文件的md5值是否和前端传入的md5值相同
        boolean checkFileMd5 = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkFileMd5) {
            R.ok().success(false).message("文件检验失败");
        }

        // 3.将文件信息传入mongodb
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setUserId(memberId);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        // 文件保存的相对路径
        String fileVarible = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        mediaFile.setFilePath(fileVarible);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);
        //向MQ发送视频处理消息
        sendProcessVideoMsg(mediaFile.getFileId());
        return R.ok().message("上传成功");
    }

    private void sendProcessVideoMsg(String mediaId) {
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent())
            return;
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String jsonString = JSON.toJSONString(map);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, jsonString);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    // 检查文件
    private boolean checkFileMd5(File mergeFile, String md5) {
        // 创建文件输入流
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(mergeFile);
            // 得到文件的md5
            String md5Hex = DigestUtils.md5Hex(fileInputStream);
            // 和传入的MD5比较
            if (md5.equalsIgnoreCase(md5Hex)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // 合并文件
    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        try {
            // 如果合并的文件存在则删除，否则创建新文件
            if (mergeFile.exists()) {
                mergeFile.delete();
            } else {
                mergeFile.createNewFile();
            }


            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            // 创建一个写对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            byte[] b = new byte[1024];
            for (File file : chunkFileList) {
                // 创建一个都对象
                RandomAccessFile raf_read = new RandomAccessFile(file, "r");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}

package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.ImageDto;
import com.atguigu.eduservice.entity.ImageRecoGrpc;
import com.atguigu.eduservice.entity.ImageRecoOuterClass;
import com.atguigu.eduservice.service.AIService;
import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AIServiceImpl implements AIService {


    private final static String filaPath = "F:\\image\\";

    @Autowired
    private ImageRecoGrpc.ImageRecoBlockingStub block;

    @Autowired
    private VodClient vodClient;


    @Override
    public R getAllImage(MultipartFile file, String modelName) {
        List<ImageDto> resList = new ArrayList<>();
        try {
            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            //文件名称
            String filename = file.getOriginalFilename();
            // 转换为图片流
            BufferedImage image = ImageIO.read(inputStream);
            String allPath = filaPath + filename;
            OutputStream out = new FileOutputStream(allPath);
            boolean isOk = ImageIO.write(image, "jpg", out);
            if (isOk) {
                ImageRecoOuterClass.ImageDto build = ImageRecoOuterClass.ImageDto.newBuilder().setImagePath(allPath + "@" + modelName).build();
                ProtocolStringList listPathList = block.getAllImageFromAI(build).getListPathList();
                for (ByteString bytes : listPathList.asByteStringList()) {
                    String cntFilePath = new String(bytes.toByteArray());
                    String[] split = cntFilePath.split("@");
                    cntFilePath = split[0];
                    File tmpFile = new File(cntFilePath);
                    FileInputStream fis = new FileInputStream(tmpFile);
                    MultipartFile multipartFile = new MockMultipartFile("file", tmpFile.getName(), ContentType.IMAGE_JPEG.getMimeType(), fis);
                    R r = vodClient.uploadOssFile(multipartFile);
                    if (r.getSuccess()) {
                        String res = (String) r.getData().get("url");
                        resList.add(new ImageDto(res, split[1], split[2]));
                    }
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 原始图片
            R r = vodClient.uploadOssFile(file);
            String originalFileImage = (String) r.getData().get("url");
            return R.ok().data("list", resList).data("originalFileImage", originalFileImage);
        }
    }
}

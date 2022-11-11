package com.atguigu.eduservice.service;

import com.atguigu.commonutils.R;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {
    R getAllImage(MultipartFile file, String modelName) ;
}

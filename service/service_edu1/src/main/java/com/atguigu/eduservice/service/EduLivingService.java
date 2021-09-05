package com.atguigu.eduservice.service;

import com.atguigu.commonutils.R;

import com.atguigu.eduservice.entity.EduLiving;
import com.atguigu.eduservice.entity.MessageType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-06-01
 */

public interface EduLivingService extends IService<EduLiving> {

    R addLive(Map<String, String> teacherInfo, EduLiving eduLiving);

    void getLive();

    List<EduLiving> getLiveById(String userId);

    R removeLivingById(String teacherId);

    List<EduLiving> getAllLivingList();


    EduLiving getLiveInfoById(String teacherId);

    R uploadAlyiVideo(MultipartFile file, String currendLivingId, HttpServletRequest request);

    R deleteAliyunvod(String id, HttpServletRequest request);

    List<EduLiving> getAllLivingAge();


    R getLiveInfoByIdAgo(String id);

    void insert(MessageType message);

    R getAllRecordById(String id,String memberId);
}

package com.atguigu.livingservice.service;

import com.atguigu.commonutils.R;
import com.atguigu.livingservice.entity.EduLiving;
import com.baomidou.mybatisplus.extension.service.IService;

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
}

package com.atguigu.livingservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.R;
import com.atguigu.livingservice.config.AliyunLiveConfig;
import com.atguigu.livingservice.entity.EduLiving;
import com.atguigu.livingservice.mapper.EduLivingMapper;
import com.atguigu.livingservice.service.EduLivingService;
import com.atguigu.livingservice.util.AliyunLiveUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zhang zhengxu
 * @since 2021-06-01
 */
@Service
public class EduLivingServiceImpl extends ServiceImpl<EduLivingMapper, EduLiving> implements EduLivingService {

    @Autowired
    private AliyunLiveConfig aliyunLiveConfig;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public R addLive( Map<String, String> teacherInfo, EduLiving eduLiving) {


        String teacherId = teacherInfo.get("id");

        String eduLivingJson = redisTemplate.opsForValue().get(teacherId);
        if(!StringUtils.isEmpty(eduLivingJson)){
            return R.error().message("您已经发起了直播,不能重复发起");
        }
        EduLiving finalLiving=new EduLiving();
        BeanUtils.copyProperties(eduLiving,finalLiving);
        finalLiving.setTeacherId(teacherId);
        finalLiving.setTeacherName(teacherInfo.get("nickname"));

        /**
         * 注意，推流要在播流域名里面生成
         */
        String pushUrl = AliyunLiveUtil.createPushUrl(teacherId, aliyunLiveConfig);
        finalLiving.setPushUrl(pushUrl);
        Map<String, String> pullUrl = AliyunLiveUtil.createPullUrl(teacherId, aliyunLiveConfig);
        finalLiving.setPullUrl(pullUrl.get("rtmpUrl"));

        baseMapper.insert(finalLiving);
        String finalLivingJson = JSONObject.toJSONString(finalLiving);
        redisTemplate.opsForValue().set(teacherId+"_living",finalLivingJson,Integer.parseInt(eduLiving.getTimeHours()), TimeUnit.HOURS);
        String id = finalLiving.getId();
        rabbitTemplate.convertAndSend("living-event-exchange","living.create.living",id);
        return R.ok().data("finalLiving",finalLiving).message("创建直播流成功");
    }

    @Override
    public void getLive() {
        String str="safa";
        System.out.println(str);
    }

    @Override
    public List<EduLiving> getLiveById(String userId) {
        List<EduLiving> list=new ArrayList<>();
        if(!userId.equals(1)){
            String livingJson = redisTemplate.opsForValue().get(userId + "_living");
            EduLiving eduLiving= JSONObject.parseObject(livingJson,EduLiving.class);
            list.add(eduLiving);
        }
        return list;
    }


    @Override
    public R removeLivingById(String teacherId) {

        String living = redisTemplate.opsForValue().get(teacherId + "_living");
        if(StringUtils.isEmpty(living)){
            return R.error().message("当前直播不存在");
        }

        redisTemplate.delete(teacherId+"_living");
        return R.ok();
    }

    @Override
    public List<EduLiving> getAllLivingList() {

        Set<String> keys = redisTemplate.keys("*" + "_living");

        List<EduLiving> list= new ArrayList<>();
        for (String key : keys) {
            EduLiving eduLiving = JSONObject.parseObject(redisTemplate.opsForValue().get(key), EduLiving.class);
            eduLiving.setPushUrl("");
            list.add(eduLiving);
        }

        return list;

    }

    @Override
    public EduLiving getLiveInfoById(String teacherId) {

        String livingInfo = redisTemplate.opsForValue().get(teacherId + "_living");
        if(StringUtils.isEmpty(livingInfo)){
            return null;
        }

        return JSONObject.parseObject(livingInfo,EduLiving.class);

    }
}

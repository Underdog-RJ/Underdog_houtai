package com.atguigu.eduservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.R;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.config.AliyunLiveConfig;
import com.atguigu.eduservice.dao.LivingTableDao;
import com.atguigu.eduservice.entity.EduLiving;
import com.atguigu.eduservice.entity.MessageResult;
import com.atguigu.eduservice.entity.MessageType;
import com.atguigu.eduservice.mapper.EduLivingMapper;
import com.atguigu.eduservice.service.EduLivingService;
import com.atguigu.eduservice.util.AliyunLiveUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private LivingTableDao livingTableDao;

    @Autowired
    private VodClient vodClient;

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
//        finalLiving.setPullUrl(pullUrl.get("rtmpUrl"));
        finalLiving.setPullUrl(pullUrl.get("m3u8Url"));

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
            String id="";
            if(!StringUtils.isEmpty(eduLiving)){
                eduLiving.setType(1);
                id=eduLiving.getId();
                list.add(eduLiving);
            }
            QueryWrapper<EduLiving> wrapper=new QueryWrapper<>();
            wrapper.eq("teacher_id",userId);
            if(!StringUtils.isEmpty(id)){
                wrapper.ne("id",id);
            }

            List<EduLiving> list1 = baseMapper.selectList(wrapper);
            //list1.forEach(item->StringUtils.isEmpty(item.getVideoId())?item.setType(2):item.setType(3));
            list1.forEach(item->{
                if(StringUtils.isEmpty(item.getVideoId())){
                    item.setType(2);
                }else {
                    item.setType(3);
                }
            });
            list.addAll(list1);

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


    @Override
    public R uploadAlyiVideo(MultipartFile file, String currendLivingId, HttpServletRequest request) {
        String token = request.getHeader("token");
        R r = vodClient.uploadAlyiVideo(file, token);
        Map<String, Object> data = r.getData();
        if(data.containsKey("videoId")){
            String videoId=(String) data.get("videoId");
            EduLiving eduLiving = baseMapper.selectById(currendLivingId);
            eduLiving.setVideoId(videoId);
            baseMapper.updateById(eduLiving);
            eduLiving.setType(3);
            return R.ok().data("eduLiving",eduLiving);
        }
        return R.error();
    }

    @Override
    public R deleteAliyunvod(String id, HttpServletRequest request) {
        EduLiving eduLiving = baseMapper.selectById(id);
        String videoId = eduLiving.getVideoId();
        if(!StringUtils.isEmpty(videoId)){
             vodClient.removeAlyVideo(videoId);
             eduLiving.setVideoId("");
             baseMapper.updateById(eduLiving);
             return R.ok();
        }
        return R.error();
    }

    @Override
    public List<EduLiving> getAllLivingAge() {
        QueryWrapper<EduLiving> wrapper=new QueryWrapper<>();
        wrapper.ne("video_id","");
        //wrapper.last("video_id is not ''");
        List<EduLiving> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public R getLiveInfoByIdAgo(String id) {
        EduLiving eduLiving = baseMapper.selectById(id);

        String videoId = eduLiving.getVideoId();

        Map<String, Object> data = vodClient.getPlayAuth(videoId).getData();
        if(data.containsKey("playAuth")){
            String playAuth= (String) data.get("playAuth");
            return R.ok().data("livingInfo",eduLiving).data("playAuth",playAuth);
        }
        return R.error();

    }

    @Override
    public void insert(MessageType message) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        message.setDate(sdf.format(new Date()));
        livingTableDao.insert(message);
    }

    @Override
    public R getAllRecordById(String id,String memberId) {
        List<MessageType> list = livingTableDao.findByLivingIdOrderByDate(id);
        List<MessageResult> finalList=new ArrayList<>();
        for (MessageType messageType : list) {
            if(memberId.equals(messageType.getUserId())){
                MessageResult messageResult=new MessageResult();
                messageResult.setMessage(messageType.getMessage());
                messageResult.setType(1);
                messageResult.setAvatar(messageType.getUserAvatar());
                finalList.add(messageResult);
            }else {
                MessageResult messageResult=new MessageResult();
                messageResult.setMessage(messageType.getMessage());
                messageResult.setType(2);
                messageResult.setAvatar(messageType.getUserAvatar());
                finalList.add(messageResult);
            }
        }
        return R.ok().data("messageList",finalList);
    }
}

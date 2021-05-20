package com.atguigu.eduservice.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 建立用户ID与通道的关联
 */
public class UserChannelMap {

    //用于保存用户id与通道的map对象
    private static Map<String, Channel> userChannelMap;

    static {
        userChannelMap =new HashMap<>();
    }

    /**
     * 添加用户Id与channel的关联
     * @param userid
     * @param channel
     */
    public static void put(String userid,Channel channel){
        userChannelMap.put(userid,channel);
    }


    /**
     * 移除用户id与channel的关联
     * @param userid
     */
    public static void remove(String userid){
        userChannelMap.remove(userid);
    }

    /**
     * 打印所有 的用户与通道的关联数据
     */
    public static void print(){
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户Id:"+s+"通道"+userChannelMap.get(s).id());
        }
    }



}

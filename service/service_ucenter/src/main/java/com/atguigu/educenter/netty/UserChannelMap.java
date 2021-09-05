package com.atguigu.educenter.netty;

import io.netty.channel.Channel;
import org.springframework.util.StringUtils;

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
     * 根据移除用户id与channel的关联
     * @param userid
     */
    public static void remove(String userid){
        userChannelMap.remove(userid);
    }

    /**
     * 根据通道id删除与channel的关联
     */
    public static void removeByChannelId(String channelId){
        if(StringUtils.isEmpty(channelId)){
            return;
        }
        for (String s : userChannelMap.keySet()) {
            Channel channel = userChannelMap.get(s);
            if(channelId.equals(channel.id().asLongText())){
                System.out.println("客户端连接断开，取消用于："+s+"与通道"+channelId+"的关联");
                userChannelMap.remove(s);
                break;
            }
        }
    }

    /**
     * 打印所有 的用户与通道的关联数据
     */
    public static void print(){
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户Id:"+s+"通道"+userChannelMap.get(s).id());
        }
    }


    /**
     * 根据好友id获取对应的通道
     * @param friendId
     * @return
     */
    public static Channel get(String friendId) {
        return userChannelMap.get(friendId);
    }
}

package com.atguigu.livingservice.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.atguigu.livingservice.config.AliyunLiveConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AliyunLiveUtil {

    private static final Logger log = LoggerFactory.getLogger(AliyunLiveUtil.class);

    /**
     * 推拉流地址示例：
     * rtmp://www.ttest.ygdjonline.com/a/a?auth_key=1558065152-0-0-c3cb54d946c0590ca9aeee63573201ee
     * 播流地址
     * 原画
     * rtmp://www.btest.ygdjonline.com/a/a?auth_key=1558065152-0-0-fc711455c0815aeb581385f33451d5b4
     * http://www.btest.ygdjonline.com/a/a.flv?auth_key=1558065152-0-0-221abff1da1ee32151e365cf0dd42a53
     * http://www.btest.ygdjonline.com/a/a.m3u8?auth_key=1558065152-0-0-72124fcc3aee3404b0d65dcc114e207f
     */

    /**
     * 根据源id创建该id的推流url
     *
     * @param sourceId
     * @param aliyunLiveConfig
     * @return
     */
    public static String createPushUrl(String sourceId, AliyunLiveConfig aliyunLiveConfig) {

        // 推流域名
        String pushDomain = aliyunLiveConfig.getAliyunLivePushDomain();
        // 应用名称
        String appName = aliyunLiveConfig.getAliyunLiveAppName();
        // 流名称
        String streamName =aliyunLiveConfig.getAliyunLiveStreamName()+sourceId;
        // 推流签名key
        String pushIdentKey = aliyunLiveConfig.getAliyunLivePushIdentKey();
        // 签名url有效时间
        Integer identUrlValidTime = aliyunLiveConfig.getAliyunLiveIdentUrlValidTime();

        // 计算过期时间
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000) + identUrlValidTime);

        // 组合推流域名前缀
//      rtmp://{pushDomain}/{appName}/{streamName}
        String rtmpUrl = StrUtil.format("rtmp://{}/{}/{}", pushDomain, appName, streamName);
        log.debug("推流域名前缀，rtmpUrl=" + rtmpUrl);

        // 组合md5加密串
//      /{appName}/{streamName}-{timestamp}-0-0-{pushIdentKey}
        String md5Url = StrUtil.format("/{}/{}-{}-0-0-{}", appName, streamName, timestamp, pushIdentKey);

        // md5加密
        String md5Str = DigestUtil.md5Hex(md5Url);
        log.debug("md5加密串，md5Url=" + md5Url + "------md5加密结果，md5Str=" + md5Str);

        // 组合最终鉴权过的推流域名
//      {rtmpUrl}?auth_key={timestamp}-0-0-{md5Str}
        String finallyPushUrl = StrUtil.format("{}?auth_key={}-0-0-{}", rtmpUrl, timestamp, md5Str);
        log.debug("最终鉴权过的推流域名=" + finallyPushUrl);

        return finallyPushUrl;
    }

    /**
     * 创建拉流域名，key=rtmpUrl、flvUrl、m3u8Url，代表三种拉流类型域名
     *
     * @param sourceId
     * @param aliyunLiveConfig
     * @return
     */
    public static Map<String, String> createPullUrl(String sourceId,  AliyunLiveConfig aliyunLiveConfig) {

        // 拉流域名
        String pullDomain = aliyunLiveConfig.getAliyunLivePullDomain();
        // 应用名称
        String appName = aliyunLiveConfig.getAliyunLiveAppName();
        // 流名称
        String streamName = aliyunLiveConfig.getAliyunLiveStreamName()+sourceId;
        // 拉流签名key
        String pullIdentKey = aliyunLiveConfig.getAliyunLivePullIdentKey();
        // 签名url有效时间
        Integer identUrlValidTime = aliyunLiveConfig.getAliyunLiveIdentUrlValidTime();

        // 计算过期时间
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000) + identUrlValidTime);

        // 组合通用域名
//      {pullDomain}/{appName}/{streamName}
        String pullUrl = StrUtil.format("{}/{}/{}", pullDomain, appName, streamName);
        log.debug("组合通用域名，pullUrl=" + pullUrl);

        // 组合md5加密串
//      /{appName}/{streamName}-{timestamp}-0-0-{pullIdentKey}
        String md5Url = StrUtil.format("/{}/{}-{}-0-0-{}", appName, streamName, timestamp, pullIdentKey);
        String md5FlvUrl = StrUtil.format("/{}/{}.flv-{}-0-0-{}", appName, streamName, timestamp, pullIdentKey);
        String md5M3u8Url = StrUtil.format("/{}/{}.m3u8-{}-0-0-{}", appName, streamName, timestamp, pullIdentKey);

        // md5加密
        String md5Str = DigestUtil.md5Hex(md5Url);
        String md5FlvStr = DigestUtil.md5Hex(md5FlvUrl);
        String md5M3u8Str = DigestUtil.md5Hex(md5M3u8Url);
        log.debug("md5加密串，md5Url    =" + md5Url + "       ------     md5加密结果，md5Str=" + md5Str);
        log.debug("md5加密串，md5FlvUrl =" + md5FlvUrl + "    ------    md5加密结果，md5FlvStr=" + md5FlvStr);
        log.debug("md5加密串，md5M3u8Url=" + md5M3u8Url + "   ------    md5加密结果，md5M3u8Str=" + md5M3u8Str);

        // 组合三种拉流域名前缀
//        rtmp://{pullUrl}?auth_key={timestamp}-0-0-{md5Str}
        String rtmpUrl = StrUtil.format("rtmp://{}?auth_key={}-0-0-{}", pullUrl, timestamp, md5Str);
//        http://{pullUrl}.flv?auth_key={timestamp}-0-0-{md5FlvStr}
        String flvUrl = StrUtil.format("http://{}.flv?auth_key={}-0-0-{}", pullUrl, timestamp, md5FlvStr);
//        http://{pullUrl}.m3u8?auth_key={timestamp}-0-0-{md5M3u8Str}
        String m3u8Url = StrUtil.format("http://{}.m3u8?auth_key={}-0-0-{}", pullUrl, timestamp, md5M3u8Str);

        log.debug("最终鉴权过的拉流rtmp域名=" + rtmpUrl);
        log.debug("最终鉴权过的拉流flv域名 =" + flvUrl);
        log.debug("最终鉴权过的拉流m3u8域名=" + m3u8Url);

        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("rtmpUrl", rtmpUrl);
        urlMap.put("flvUrl", flvUrl);
        urlMap.put("m3u8Url", m3u8Url);

        return urlMap;
    }
}

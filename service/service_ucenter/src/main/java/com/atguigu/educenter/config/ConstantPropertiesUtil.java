package com.atguigu.educenter.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${oauth.appid}")
    private String oauthAppId;

    @Value("${oauth.token}")
    private String oauthToken;

    public static String OAUTH_APPID;
    public static String OAUTH_TOKEN;
    public static String OAUTH_TYPE;

    @Override
    public void afterPropertiesSet() throws Exception {
        OAUTH_APPID=oauthAppId;
        OAUTH_TOKEN=oauthToken;
        OAUTH_TYPE="get_user_info";


    }
}

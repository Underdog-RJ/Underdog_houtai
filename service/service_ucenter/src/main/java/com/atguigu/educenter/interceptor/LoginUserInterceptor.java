package com.atguigu.educenter.interceptor;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exception.BizCodeEnume;
import com.atguigu.servicebase.exceptionhandler.UnLoginException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = JwtUtils.checkToken(request);
        if (!flag) {
            throw new UnLoginException(BizCodeEnume.UNLOGIN_EXCEPTION.getCode(), BizCodeEnume.UNLOGIN_EXCEPTION.getMsg());
        }
        return true;
    }
}

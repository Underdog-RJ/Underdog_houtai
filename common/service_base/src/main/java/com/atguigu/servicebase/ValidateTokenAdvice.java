package com.atguigu.servicebase;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exception.BizCodeEnume;
import com.atguigu.servicebase.exceptionhandler.InValidToken;
import com.atguigu.servicebase.exceptionhandler.UnLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 次注解为了判断请求所需资源时是否登录
 */
@Aspect
@Component
@Order(1)
public class ValidateTokenAdvice {

    @Pointcut("@annotation(com.atguigu.servicebase.anno.ValidateToken)")
    private void ValidateTokenAdvice() {

    }

    @Around("ValidateTokenAdvice()")
    public Object judgeShowData(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");

        if (StringUtils.isEmpty(token))
            throw new UnLoginException(BizCodeEnume.UNLOGIN_EXCEPTION.getCode(), BizCodeEnume.UNLOGIN_EXCEPTION.getMsg());
        boolean flag = JwtUtils.checkToken(token);
        if (!flag)
            throw new InValidToken();
        return point.proceed();
    }


}

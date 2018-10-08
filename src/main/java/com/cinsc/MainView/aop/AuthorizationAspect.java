package com.cinsc.MainView.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author: 束手就擒
 * @Date: 18-10-8 下午2:10
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class AuthorizationAspect {

    @Pointcut("execution(public * com.cinsc.MainView.ctr.ArrangeController.*(..))")
    public void pointCut(){}

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("【注解：Before】浏览器输入的网址=URL : " + request.getRequestURL().toString() + ", HTTP_METHOD : " + request.getMethod() + ", 执行的业务方法名=CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + ", 业务方法获得的参数=ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }
}


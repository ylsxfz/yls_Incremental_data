package com.fengtu.data.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author yls
 * @Date 2020/4/14 10:41
 * @Description
 * @Version 1.0
 **/
@Aspect
@Component
public class AppContainerAspect {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(AppContainerAspect.class);

    /**
     * @Author yls
     * @Description 切入点
     * @Date 2020/3/30 17:15
     * @param
     * @return void
     **/
    @Pointcut("execution(public * com.fengtu.data.service.*.*(..))")
    public void appContainerAspect(){

    }

    /**
     * @Author yls
     * @Description 前置通知
     * @Date 2020/3/30 17:15
     * @param joinPoint
     * @return void
     **/
    @Before(value = "appContainerAspect()")
    public void before(JoinPoint joinPoint){
        String name = joinPoint.getSignature().getName();
        LOGGER.info("方法开始执行==》"+name);
    }

    /**
     * @Author yls
     * @Description 后置通知
     * @Date 2020/3/30 17:15
     * @param joinPoint
     * @return void
     **/
    @After(value = "appContainerAspect()")
    public void after(JoinPoint joinPoint){
        String name = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("方法执行参数==》"+joinPoint.toString());
        LOGGER.info(name+"方法执行结束...."+ Arrays.toString(args));
    }

    /**
     * @Author yls
     * @Description 返回通知
     * @Date 2020/3/30 17:16
     * @param joinPoint
     * @param result 返回的结果
     * @return void
     **/
    @AfterReturning(value = "appContainerAspect()",returning = "result")
    public void afterReturning(JoinPoint joinPoint,Object result){
        String name = joinPoint.getSignature().getName();
        LOGGER.info(name+"方法返回值...."+result);
    }

    /**
     * @Author yls
     * @Description 异常通知
     * @Date 2020/3/30 17:17
     * @param joinPoint
     * @param e 异常
     * @return void
     **/
    @AfterThrowing(value = "appContainerAspect()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e){
        String name = joinPoint.getSignature().getName();
        LOGGER.info(name+"方法抛出异常，异常是："+e);
    }

    /**
     * @Author yls
     * @Description 环绕通知
     * @Date 2020/3/30 17:17
     * @param pjq
     * @return java.lang.Object
     **/
    @Around("appContainerAspect()")
    public Object around(ProceedingJoinPoint pjq)throws Throwable{
        return pjq.proceed();
    }
}

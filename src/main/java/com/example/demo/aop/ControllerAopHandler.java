package com.example.demo.aop;


import com.alibaba.fastjson.JSON;
import com.example.demo.domain.DemoResponse;
import groovy.util.logging.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@lombok.extern.slf4j.Slf4j
@Aspect
@Component
@Slf4j
public class ControllerAopHandler {
    @Around("execution(* com.example.demo.controller..*(..))") // 替换为你的 controller 包路径
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed(); // 执行方法
        } catch (Throwable throwable) {
            log.error("error", throwable);
            return new DemoResponse<>().fail("500", throwable.getMessage());
        } finally {
            long executionTime = System.currentTimeMillis() - start;

            String methodName = joinPoint.getSignature().toShortString();
            System.out.println(methodName + " executed in " + executionTime + "ms");

        }

        return proceed;
    }
}

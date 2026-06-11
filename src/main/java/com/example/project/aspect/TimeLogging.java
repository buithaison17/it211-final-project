package com.example.project.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeLogging {
    @Around("execution(* com.example.project.service.*.*(..))")
    public Object timeExcute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        // Lấy tên hàm
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Time logging: " + methodName + " - " + (end - start) + "ms");
        return result;
    }
}

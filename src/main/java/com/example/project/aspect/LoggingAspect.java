package com.example.project.aspect;

import com.example.project.model.entity.Booking;
import com.example.project.model.entity.User;
import com.example.project.security.principal.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @AfterReturning(
            value = "execution(* com.example.project.service.BookingService.bookCourt(..))",
            returning = "result"
    )
    public void bookingAspectReturn(JoinPoint joinPoint, Object result) {
        Booking booking = (Booking) result;
        log.info("[AUDIT - SUCCESS] Khách hàng {} đặt thành công Sân {} vào khoảng thời gian {} - {}",
                booking.getUser().getId(),
                booking.getCourt().getId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }

    @AfterThrowing(
            value = "execution(* com.example.project.service.BookingService.bookCourt(..))",
            throwing = "exception"
    )
    public void bookingThrow(JoinPoint joinPoint, Exception exception) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error("[AUDIT - FAILED] Khách hàng {} cố gắng đặt Sân {} nhưng thất bại do {}",
                userPrincipal.getUser().getId(),
                joinPoint.getArgs()[0],
                exception.getMessage()
        );
    }
}

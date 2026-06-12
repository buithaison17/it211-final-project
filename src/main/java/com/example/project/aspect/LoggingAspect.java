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
        log.info("Người dùng {} đặt sân {} thời gian {} - {}",
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
        log.error("Người dùng {} không thể đặt sân vì {}",
                userPrincipal.getUser().getId(),
                exception.getMessage()
        );
    }
}

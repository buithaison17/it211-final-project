package com.example.project.exception;

import com.example.project.model.dto.response.ApiDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiDataResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    // Lỗi khi không tìm thấy
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiDataResponse<?>> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.NOT_FOUND
        ), HttpStatus.NOT_FOUND);
    }

    // Lỗi khi refresh token
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<ApiDataResponse<?>> handleRefreshTokenInvalidException(RefreshTokenInvalidException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.FORBIDDEN
        ), HttpStatus.FORBIDDEN);
    }

    // Lỗi dữ liệu đầu vào không hợp lệ
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiDataResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                "Lỗi đầu vào",
                null,
                errors,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageExtensionInvalidException.class)
    public ResponseEntity<ApiDataResponse<?>> handleImageExtensionInvalidException(ImageExtensionInvalidException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiDataResponse<?>> handleUnauthorizedException(UnauthorizedException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.UNAUTHORIZED
        ), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ApiDataResponse<?>> handlePasswordIncorrectException(PasswordIncorrectException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistException.class)
    public ResponseEntity<ApiDataResponse<?>> handleExistException(ExistException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StartTimeAfterEndTimeException.class)
    public ResponseEntity<ApiDataResponse<?>> handleStartTimeAfterEndTimeException(StartTimeAfterEndTimeException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TimeDurationHaveBookingException.class)
    public ResponseEntity<ApiDataResponse<?>> handleTimeDurationHaveBookingException(TimeDurationHaveBookingException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotMatchOTPException.class)
    public ResponseEntity<ApiDataResponse<?>> handleNotMatchOTPException(NotMatchOTPException exception) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                false,
                exception.getMessage(),
                null,
                null,
                HttpStatus.BAD_REQUEST
        ), HttpStatus.BAD_REQUEST);
    }
}

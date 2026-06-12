package com.example.project.exception;

public class NotMatchOTPException extends RuntimeException {
    public NotMatchOTPException(String message) {
        super(message);
    }
}

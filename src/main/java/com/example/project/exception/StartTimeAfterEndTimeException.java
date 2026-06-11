package com.example.project.exception;

public class StartTimeAfterEndTimeException extends RuntimeException {
    public StartTimeAfterEndTimeException(String message) {
        super(message);
    }
}

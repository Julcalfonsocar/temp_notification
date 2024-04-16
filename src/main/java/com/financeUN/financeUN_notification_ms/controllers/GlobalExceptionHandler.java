package com.financeUN.financeUN_notification_ms.controllers;

import com.financeUN.financeUN_notification_ms.services.NotificationServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotificationServiceException.class)
    public ResponseEntity<String> handleNotificationServiceException(NotificationServiceException e) {
        // Loguear el error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
    }
}

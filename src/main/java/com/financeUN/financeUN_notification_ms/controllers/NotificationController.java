package com.financeUN.financeUN_notification_ms.controllers;

import com.financeUN.financeUN_notification_ms.model.Notification;
import com.financeUN.financeUN_notification_ms.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@Validated
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendNotification(@Valid @RequestBody Notification notification) {
        notificationService.sendAchievementNotification(notification.getUserId(), notification.getType(), notification.getMessage());
        return ResponseEntity.ok("Notification sent successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@PathVariable int userId) {
        List<Notification> notifications = notificationService.getAllNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/view/{notificationId}")
    public ResponseEntity<String> markNotificationAsViewed(@PathVariable int notificationId) {
        notificationService.markNotificationAsViewed(notificationId);
        return ResponseEntity.ok("Notification marked as viewed successfully.");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteAllNotificationsForUser(@PathVariable int userId) {
        notificationService.deleteAllNotificationsForUser(userId);
        return ResponseEntity.ok("All notifications for user deleted successfully.");
    }

    @DeleteMapping("/viewed/{notificationId}")
    public ResponseEntity<String> deleteViewedNotification(@PathVariable int notificationId) {
        notificationService.deleteViewedNotification(notificationId);
        return ResponseEntity.ok("Viewed notification deleted successfully.");
    }

    // Manejo de excepciones
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Obtener los mensajes de error de validación
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Construir un mensaje de error
        String errorMessage = "Validation error: " + String.join(", ", errors);

        // Retornar una respuesta con el mensaje de error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}

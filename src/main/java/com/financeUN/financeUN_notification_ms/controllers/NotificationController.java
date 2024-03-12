package com.financeUN.financeUN_notification_ms.controllers;

import com.financeUN.financeUN_notification_ms.model.Notification;
import com.financeUN.financeUN_notification_ms.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Endpoint para enviar una nueva notificación de logro
    @PostMapping("/send")
    public void sendNotification(@RequestBody Notification notification) {
        notificationService.sendAchievementNotification(notification.getUserId(), notification.getType(), notification.getMessage());
    }

    // Endpoint para obtener todas las notificaciones de un usuario
    @GetMapping("/user/{userId}")
    public List<Notification> getAllNotificationsForUser(@PathVariable int userId) {
        return notificationService.getAllNotificationsForUser(userId);
    }

    // Endpoint para marcar una notificación como vista
    @PutMapping("/view/{userId}/{notificationId}")
    public void markNotificationAsViewed(@PathVariable int userId, @PathVariable int notificationId) {
        notificationService.markNotificationAsViewed(userId, notificationId);
    }

    // Endpoint para eliminar todas las notificaciones de un usuario
    @DeleteMapping("/user/{userId}")
    public void deleteAllNotificationsForUser(@PathVariable int userId) {
        notificationService.deleteAllNotificationsForUser(userId);
    }

    // Endpoint para eliminar una notificación específica que ya ha sido vista
    @DeleteMapping("/viewed/{userId}/{notificationId}")
    public void deleteViewedNotification(@PathVariable int userId, @PathVariable int notificationId) {
        notificationService.deleteViewedNotificationForUser(userId, notificationId);
    }


}

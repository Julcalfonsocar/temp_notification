package com.financeUN.financeUN_notification_ms.services;

import com.financeUN.financeUN_notification_ms.model.Notification;
import com.financeUN.financeUN_notification_ms.model.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    private static final String NOTIFICATION_KEY_PREFIX = "notification:";
    private static final String NOTIFICATION_ID_KEY = "notification:id";

    @Autowired
    private RedisTemplate<String, Notification> redisTemplate;

    @Autowired
    private RedisTemplate<String, Integer> intRedisTemplate;

    public int generateNotificationId() {
        ValueOperations<String, Integer> ops = intRedisTemplate.opsForValue();
        return ops.increment(NOTIFICATION_ID_KEY, 1).intValue();
    }

    public void sendAchievementNotification(int userId, NotificationType type, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setViewed(false);

        // Guardar la notificación en Redis
        saveNotification(userId, notification);

        // Aquí iría la lógica para enviar la notificación al usuario
    }


    private void saveNotification(int userId, Notification notification) {
        // La clave incluirá el ID del usuario y el ID de la notificación
        int notificationId = generateNotificationId();
        notification.setId(notificationId);
        String key = NOTIFICATION_KEY_PREFIX + userId + ":" + notificationId;
        redisTemplate.opsForValue().set(key, notification);
    }


    public List<Notification> getAllNotificationsForUser(int userId) {
        // Encuentra todas las claves que coincidan con el patrón del ID del usuario
        Set<String> keys = redisTemplate.keys("notification:" + userId + ":*");
        List<Notification> notifications = new ArrayList<>();
        if (keys != null) {
            for (String key : keys) {
                Notification notification = redisTemplate.opsForValue().get(key);
                if (notification != null) {
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    public Notification getNotification(int userId, int notificationId) {
        String key = "notification:" + userId + ":" + notificationId;
        return redisTemplate.opsForValue().get(key);
    }

    public void markNotificationAsViewed(int userId, int notificationId) {
        String key = NOTIFICATION_KEY_PREFIX + userId + ":" + notificationId;
        Notification notification = redisTemplate.opsForValue().get(key);
        if (notification != null) {
            notification.setViewed(true);
            // Actualiza la notificación existente en lugar de crear una nueva
            redisTemplate.opsForValue().set(key, notification);
        }
    }


    public void deleteAllNotificationsForUser(int userId) {
        // Encuentra todas las claves que coincidan con el patrón del ID del usuario
        Set<String> keys = redisTemplate.keys("notification:" + userId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteViewedNotificationForUser(int userId, int notificationId) {
        Notification notification = getNotification(userId, notificationId);
        if (notification != null && notification.isViewed()) {
            String key = "notification:" + userId + ":" + notificationId;
            redisTemplate.delete(key);
        }
    }
}

package com.financeUN.financeUN_notification_ms.services;

import com.financeUN.financeUN_notification_ms.exceptions.NotificationNotFoundException;
import com.financeUN.financeUN_notification_ms.exceptions.NotificationNotViewedException;
import com.financeUN.financeUN_notification_ms.model.Notification;
import com.financeUN.financeUN_notification_ms.model.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class NotificationService {

    private static final String NOTIFICATION_KEY_PREFIX = "notification:";
    private static final String NOTIFICATION_ID_KEY = "notification:id";

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    @Autowired
    private RedisTemplate<String, Notification> redisTemplate;

    @Autowired
    private RedisTemplate<String, Integer> intRedisTemplate;

    public int generateNotificationId() {
        ValueOperations<String, Integer> ops = intRedisTemplate.opsForValue();
        return ops.increment(NOTIFICATION_ID_KEY, 1).intValue();
    }

    public void sendAchievementNotification(int userId, NotificationType type, String message) {   
        try {
            // Lógica para enviar la notificación al usuario y guardarla en Redis
            Notification notification = new Notification();
            notification.setId(generateNotificationId());
            notification.setUserId(userId);
            notification.setType(type);
            notification.setMessage(message);
            notification.setViewed(false);

            // Guardar la notificación en Redis
            saveNotification(userId, notification);

            // Aquí iría la lógica para enviar la notificación al usuario
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            // Manejo de la excepción de conexión con Redis
            // Loguear el error o tomar alguna otra acción apropiada, como enviar una respuesta de error al cliente
            throw new NotificationServiceException("Error connecting to Redis", e);
        }

    }

    private void saveNotification(int userId, Notification notification) {
        String key = NOTIFICATION_KEY_PREFIX + userId + ":" + notification.getId();
        redisTemplate.opsForValue().set(key, notification);
    }

    public List<Notification> getAllNotificationsForUser(int userId) {
        // Encuentra todas las claves que coincidan con el patrón del ID del usuario
        Set<String> keys = redisTemplate.keys(NOTIFICATION_KEY_PREFIX + userId + "*");
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
        String key = NOTIFICATION_KEY_PREFIX + userId + ":" + notificationId;
        return redisTemplate.opsForValue().get(key);
    }

    public void markNotificationAsViewed(int notificationId) {
        String key = NOTIFICATION_KEY_PREFIX + "*:" + notificationId; // Patron de la clave
        Set<String> keys = redisTemplate.keys(key);
        if (keys != null && !keys.isEmpty()) {
            String notificationKey = keys.iterator().next(); // Obtener la primera clave encontrada
            Notification notification = redisTemplate.opsForValue().get(notificationKey);
            if (notification != null) {
                notification.setViewed(true);
                // Actualizar la notificación en Redis
                redisTemplate.opsForValue().set(notificationKey, notification);
                return; // Terminar el bucle una vez que se marque la notificación como vista
            }
        }
        // Si la notificación no se encuentra, lanzar una excepción
        logger.info("Notification with ID {} not found.", notificationId); // Agregar este registro
        throw new NotificationNotFoundException("Notification with ID " + notificationId + " not found.");
    }



    public void deleteAllNotificationsForUser(int userId) {
        // Encuentra todas las claves que coincidan con el patrón del ID del usuario
        Set<String> keys = redisTemplate.keys(NOTIFICATION_KEY_PREFIX + userId + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void deleteViewedNotification(int notificationId) {
        // Buscar la notificación por su ID
        String keyPattern = NOTIFICATION_KEY_PREFIX + "*:" + notificationId; // Patrón de la clave
        Set<String> keys = redisTemplate.keys(keyPattern);
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                Notification notification = redisTemplate.opsForValue().get(key);
                if (notification != null && notification.getId() == notificationId) {
                    if (notification.isViewed()) {
                        redisTemplate.delete(key);
                        return; // Terminar el bucle una vez que se elimine la notificación vista
                    } else {
                        // Si la notificación no está vista, lanzar una excepción
                        logger.info("Notification with ID {} has not been viewed.", notificationId);
                        throw new NotificationNotViewedException("Viewed notification with ID " + notificationId + " not found.");
                    }
                }
            }
        }
        // Si la notificación no se encuentra, lanzar una excepción
        logger.info("Notification with ID {} not found.", notificationId);
        throw new NotificationNotFoundException("Notification with ID " + notificationId + " not found.");
    }


}
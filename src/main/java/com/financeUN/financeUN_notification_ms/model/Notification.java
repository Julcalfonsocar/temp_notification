package com.financeUN.financeUN_notification_ms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private int id;
    private int userId;
    private NotificationType type;
    private String message;
    private boolean viewed;
}

package com.financeUN.financeUN_notification_ms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private int id;
    @NotNull(message = "User ID cannot be null")
    private Integer userId;
    @NotNull(message = "Type cannot be null")
    private NotificationType type;
    @NotBlank(message = "Message cannot be blank")
    private String message;
    private boolean viewed;
}

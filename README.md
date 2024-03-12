Lista de endpoint

1. **Enviar una Notificación (POST):**
    - **Método:** POST
    - **URL:** `http://localhost:8080/api/notifications/send`
    - **Headers:** Key: `Content-Type`, Value: `application/json`
    - **Body (raw, JSON):**
        
        ```json
        {
          "userId": 1,
          "type": "ACHIEVEMENT",
          "message": "¡Felicidades por alcanzar tu meta!",
          "viewed": false}
        
        ```
        
2. **Obtener Todas las Notificaciones de un Usuario (GET):**
    - **Método:** GET
    - **URL:** `http://localhost:8080/api/notifications/user/{userId}` (reemplaza `{userId}` con el ID del usuario)
3. **Marcar una Notificación como Vista (PUT):**
    - **Método:** PUT
    - **URL:** `http://localhost:8080/api/notifications/view/{userId}/{notificationId}` (reemplaza `{userId}` y `{notificationId}` con los valores correspondientes)
4. **Eliminar Todas las Notificaciones de un Usuario (DELETE):**
    - **Método:** DELETE
    - **URL:** `http://localhost:8080/api/notifications/user/{userId}` (reemplaza `{userId}` con el ID del usuario)
5. **Eliminar una Notificación Específica que ha sido Vista (DELETE):**
    - **Método:** DELETE
    - **URL:** `http://localhost:8080/api/notifications/viewed/{userId}/{notificationId}` (reemplaza `{userId}` y `{notificationId}` con los valores correspondientes)

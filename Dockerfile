# Utilizar una imagen base de Java con Maven
FROM openjdk:17-jdk-alpine

# Copiar el JAR del paso de construcción
COPY target/financeUN_notification_ms-0.0.1-SNAPSHOT.jar notification-ms.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "notification-ms.jar"]
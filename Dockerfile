# Utilizar una imagen base de Java con Maven
FROM maven:3.6.3-jdk-11 as build

# Copiar los archivos del proyecto al contenedor
COPY src /home/app/src
COPY pom.xml /home/app

# Construir el proyecto y empaquetarlo
RUN mvn -f /home/app/pom.xml clean package

# Utilizar una imagen base de Java para ejecutar la aplicación
FROM openjdk:11-jre-slim

# Copiar el JAR del paso de construcción
COPY --from=build /home/app/target/financeUN_notification_ms-0.0.1-SNAPSHOT.jar /usr/local/lib/notification-ms.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/usr/local/lib/notification-ms.jar"]

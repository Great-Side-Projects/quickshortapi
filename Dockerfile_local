# Usar una imagen base de Java
FROM openjdk:18-jdk-alpine
# Copiar el archivo JAR de la aplicación al contenedor
ADD target/QuickShort-api-1.0.0-SNAPSHOT.jar /app/quickshortapi.jar
# Establecer el directorio de trabajo
WORKDIR /app
# Exponer el puerto en el que se ejecuta la aplicación Spring Boot
EXPOSE 8080
# Comando para ejecutar la aplicación Spring Boot al iniciar el contenedor
CMD ["java", "-jar", "quickshortapi.jar"]
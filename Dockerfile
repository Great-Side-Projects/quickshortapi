# Usar una imagen base de Java
FROM openjdk:18-jdk-alpine
# Establecer argumentos
ARG MONGODB_URI
ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG KAFKA_BOOTSTRAP_SERVERS
ARG KAFKA_PROPERTIES_SASL_JAAS_CONFIG
RUN echo "MONGODB_URI=${MONGODB_URI}"
# Establecer el directorio de trabajo
WORKDIR /app
# Copiar el archivo JAR de la aplicación al contenedor
COPY target/*.jar /app/quickshortapi.jar
# Exponer el puerto en el que se ejecuta la aplicación Spring Boot
EXPOSE 8080
# Comando para ejecutar la aplicación Spring Boot al iniciar el contenedor
CMD ["java", "-jar", "quickshortapi.jar",\
"--spring.data.mongodb.uri=${MONGODB_URI}",\
"--spring.data.redis.host=${REDIS_HOST}",\
"--spring.data.redis.port=${REDIS_PORT}",\
"--spring.data.redis.password=${REDIS_PASSWORD}",\
"--spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS}",\
"--spring.kafka.properties.sasl.jaas.config=${KAFKA_PROPERTIES_SASL_JAAS_CONFIG}"]
# Usar una imagen base de Java
FROM openjdk:18-jdk-alpine
# Copiar el archivo JAR de la aplicación al contenedor
ADD target/QuickShort-api-1.0.0-SNAPSHOT.jar /app/quickshortapi.jar
# Establecer el directorio de trabajo
WORKDIR /app
# Exponer el puerto en el que se ejecuta la aplicación Spring Boot
EXPOSE 8080

# Definir argumentos (variables de entorno) con valores predeterminados
ARG MONGODB_URI
ARG REDIS_HOST
ARG REDIS_PORT
ARG REDIS_PASSWORD
ARG KAFKA_BOOTSTRAP_SERVERS
ARG KAFKA_PROPERTIES_SASL_JAAS_CONFIG
ARG PRUEBA_VARIABLE

# Depurar valores de las variables de entorno
RUN echo "MONGODB_URI: ${MONGODB_URI}"
RUN echo "REDIS_HOST: ${REDIS_HOST}"
RUN echo "REDIS_PORT: ${REDIS_PORT}"
RUN echo "REDIS_PASSWORD: ${REDIS_PASSWORD}"
RUN echo "KAFKA_BOOTSTRAP_SERVERS: ${KAFKA_BOOTSTRAP_SERVERS}"
RUN echo "KAFKA_PROPERTIES_SASL_JAAS_CONFIG: ${KAFKA_PROPERTIES_SASL_JAAS_CONFIG}"
RUN echo "PRUEBA_VARIABLE: ${PRUEBA_VARIABLE}"

# Comando para ejecutar la aplicación Spring Boot al iniciar el contenedor
CMD ["java", "-jar", "quickshortapi.jar",\
"--spring.data.mongodb.uri=${MONGODB_URI}",\
"--spring.data.redis.host=${REDIS_HOST}",\
"--spring.data.redis.port=${REDIS_PORT}",\
"--spring.data.redis.password=${REDIS_PASSWORD}",\
"--spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS}",\
"--spring.kafka.properties.sasl.jaas.config=${KAFKA_PROPERTIES_SASL_JAAS_CONFIG}"]
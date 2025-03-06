FROM eclipse-temurin:17-jre-jammy

# Descargar e instalar dockerize
RUN apt-get update && apt-get install -y wget && \
    wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz && \
    tar -xzvf dockerize-linux-amd64-v0.6.1.tar.gz -C /usr/local/bin && \
    rm dockerize-linux-amd64-v0.6.1.tar.gz  # Limpiar el archivo descargado

# Copiar el archivo JAR
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Establecer el comando de entrada, primero esperar a que la base de datos esté lista y luego ejecutar la aplicación
ENTRYPOINT ["dockerize", "-wait", "tcp://db:3306", "-timeout", "60s", "java", "-jar", "/app.jar"]
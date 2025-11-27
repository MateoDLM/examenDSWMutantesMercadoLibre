# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

# Copiar archivos de Gradle primero para aprovechar caché
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# Copiar el código fuente
COPY src src

# Compilar y generar el JAR
RUN ./gradlew bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR generado desde la etapa de build
# Usamos un wildcard (*) para no depender de la versión exacta del JAR
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto por defecto de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
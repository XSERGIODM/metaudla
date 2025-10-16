# Etapa 1: Construcción
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:21-jdk-slim
COPY --from=build /app/target/metaudla-0.0.1-SNAPSHOT.jar app_metaudla.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app_metaudla.jar"]
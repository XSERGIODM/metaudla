FROM maven:3.8.4-openjdk-21 as build
COPY pom.xml /
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
RUN ls -la /target

FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/metaudla-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_metaudla.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app_metaudla.jar"]
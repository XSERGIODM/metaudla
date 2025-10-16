FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/metaudla-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_metaudla.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app_metaudla.jar"]
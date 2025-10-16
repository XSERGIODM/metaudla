FROM maven:3.9.9-openjdk-21 as build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

RUN  ls -la /app/target

FROM openjdk:21-jdk-slim
COPY --from=build /app/target/metaudla-0.0.1-SNAPSHOT.jar /app/metaudla.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/metaudla.jar"]


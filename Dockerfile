FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk
COPY --from=build /target/upsolve-0.0.1-SNAPSHOT.jar upsolve.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "upsolve.jar"]

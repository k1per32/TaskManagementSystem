FROM alpine:latest
RUN apk add openjdk17
COPY /target/TaskManagementSystem-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

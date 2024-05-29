FROM maven:3.8-openjdk-17 AS builder

USER root

WORKDIR /build

COPY . /build

RUN mvn clean package


FROM openjdk:17-alpine

USER root

RUN addgroup -g 20000 app && adduser -u 20000 -G app -D app -s /bin/bash

COPY --from=builder /build/target/*.jar /app.jar
COPY --from=builder /build/.env /application.properties

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

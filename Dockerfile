# Dockerfile for Java application with Maven
FROM openjdk:8-jdk-alpine

RUN apk update && apk add --no-cache maven # Install Maven

WORKDIR /home/eladmin

COPY . .

CMD ["sh", "-c", "mvn clean package && java -jar target/*.jar"]

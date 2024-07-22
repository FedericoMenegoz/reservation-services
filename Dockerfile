# 2nd stage, build the runtime image
FROM maven:3.9.7-eclipse-temurin-22-alpine

COPY target/reservation-services.jar .

WORKDIR .
CMD ["java", "-jar", "reservation-services.jar"]

EXPOSE 8080

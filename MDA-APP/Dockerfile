# syntax = docker/dockerfile:1.2
#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/MDA-APP-1.0-SNAPSHOT-jar-with-dependencies.jar MDA-APP.jar

#ENV NOMBRE_BOT=dds-grupo-5-bot
#ENV TOKEN_BOT=6181937594:AAH4SlguqvuPGGvl4cYunrGZ9TJuG16cjX4

EXPOSE 8080
ENTRYPOINT ["java","-classpath","MDA-APP.jar","ar.utn.dds.mda.MdaAPI"]

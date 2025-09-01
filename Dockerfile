FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .

RUN mvn -q -e -B -DskipTests dependency:go-offline
COPY src ./src
# Build
RUN mvn -q -e -B -DskipTests package


FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
# Environment can be overriden by docker-compose/.env
ENV SPRING_PROFILES_ACTIVE=test
ENTRYPOINT ["java","-jar","/app/app.jar"]
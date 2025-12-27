FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :core-api:bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/core-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

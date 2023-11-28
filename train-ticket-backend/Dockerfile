FROM eclipse-temurin:17-focal as builder

WORKDIR /app
COPY . .

RUN chmod +x gradlew && ./gradlew build -x test


FROM eclipse-temurin:17-focal

COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY --from=builder /app/src/main/resources/application.yaml /app/application.yaml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

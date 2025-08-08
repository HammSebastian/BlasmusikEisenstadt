FROM eclipse-temurin:21

WORKDIR /app

COPY backend_stadtkapelle_eisenstadt-*.jar app.jar

RUN mkdir -p /app/uploads /app/images

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

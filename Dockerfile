FROM eclipse-temurin:21

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/logback.xml /app/logback.xml

EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=prod", "-Dlogging.config=logback.xml", "-jar", "app.jar"]

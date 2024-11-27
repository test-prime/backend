FROM eclipse-temurin:21

WORKDIR /app

COPY target/demo-0.0.1-SNAPSHOT.jar.original.jar app.jar

EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=prod","-jar", "app.jar"]

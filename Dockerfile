FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/MoonRider-0.0.1-SNAPSHOT.jar /app/MoonRider.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "MoonRider.jar"]

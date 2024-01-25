FROM lippert/adoptopenjdk-17
WORKDIR /app
COPY ./target/HavenHotelApplication-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

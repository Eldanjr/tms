FROM maven:3.8.6-openjdk-18 as builder
WORKDIR /app/
COPY src /app/src
COPY pom.xml /app/
RUN mvn clean package -DskipTests
FROM openjdk:17
COPY --from=builder /app/target/Applicant-Service-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]


FROM ubuntu:latest
LABEL authors="charlesfeng"

ENTRYPOINT ["top", "-b"]

# Use the official OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/your-application.jar app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "app.jar"]

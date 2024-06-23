FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build.gradle.kts and settings.gradle.kts files
COPY build.gradle.kts settings.gradle.kts /app/

# Copy the gradle wrapper
COPY gradlew /app/gradlew
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Copy the protos
COPY protobuf /app/protobuf

# Set execute permission on gradlew
RUN chmod +x gradlew

# Download dependencies and build the application
RUN ./gradlew clean build

# Expose the port the app runs on
EXPOSE 9090

# Add a volume to store logs
VOLUME /app/logs

# The application's jar file
ARG JAR_FILE=build/libs/integration-service.jar

# Add the application's jar to the container
ADD ${JAR_FILE} integration-service.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "integration-service.jar"]

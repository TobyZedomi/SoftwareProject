# Use a lightweight Java 17 runtime base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy project files to the image
COPY . .

# Build the application using the Maven wrapper
RUN ./mvnw clean package -DskipTests

# Expose the port  app runs on
EXPOSE 8080

# Run the Spring Boot jar file
CMD ["java", "-jar", "target/SoftwareProject-0.0.1-SNAPSHOT.jar"]

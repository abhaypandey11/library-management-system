# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Fix permission issue
RUN chmod +x mvnw

# Copy source code
COPY src src

# Build the project
RUN ./mvnw clean install -DskipTests

# Run the jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/library-management.jar"]
RUN chmod +x mvnw
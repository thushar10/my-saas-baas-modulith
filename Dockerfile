# --- Stage 1: Build the application ---
# Use a JDK image to build the project
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle ./gradle

# Copy the build file and download dependencies
# This layer is cached by Docker unless build.gradle changes
COPY build.gradle .
COPY settings.gradle .
RUN ./gradlew build --no-daemon || return 0

# Copy the rest of the source code
COPY . .

# Build the application, skipping tests for faster image creation
RUN ./gradlew build -x test --no-daemon


# --- Stage 2: Create the final, lightweight image ---
# Use a JRE image which is smaller than the JDK
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy only the built JAR file from the builder stage
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8090

# The command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
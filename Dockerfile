# ==========================================
# Stage 1: Build the application
# ==========================================
FROM gradle:8.7-jdk21 AS build

# Set the working directory inside the container
WORKDIR /home/gradle/project

# Copy only the dependency-related files first to leverage Docker layer caching
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts gradlew ./

# Download dependencies (this layer will be cached unless your build files change)
#RUN ./gradlew build -x test --no-daemon || true

# Copy the actual application source code
COPY src/ src/

# Build the production-ready optimized shadow/distribution jar
RUN chmod +x gradlew
RUN ./gradlew installDist -x test --no-daemon

# ==========================================
# Stage 2: Create the minimal runtime image
# ==========================================
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks (optional but highly recommended)
RUN apk --no-cache add curl

# Create a non-root user to run the application securely
RUN addgroup -S micronaut && adduser -S micronaut -G micronaut
USER micronaut

WORKDIR /app

# Copy the built jar file from the build stage
# Micronaut 4 application plugin creates a fat/distribution jar in build/libs/
COPY --from=build /home/gradle/project/build/install/books-app /app

# Expose Micronaut's default port
EXPOSE 8080

# Run the application with optimized JVM flags
ENTRYPOINT ["/app/bin/books-app"]
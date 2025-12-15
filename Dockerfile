# Stage 1: Build stage menggunakan Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml dan download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build aplikasi
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage dengan minimal image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy JAR dari builder stage
COPY --from=builder /app/target/kasihreview-0.0.1-SNAPSHOT.jar app.jar

# Expose port (sesuaikan dengan port aplikasi Anda)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]

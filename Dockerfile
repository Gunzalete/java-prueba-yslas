# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml for dependency caching
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Download dependencies (cached layer if pom.xml doesn't change)
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copy source code and build
COPY src/ src/
RUN ./mvnw -q -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/PruebaTecnicaJavaYslas-0.0.1-SNAPSHOT.jar app.jar

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run application with optimized JVM settings
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "/app/app.jar"]

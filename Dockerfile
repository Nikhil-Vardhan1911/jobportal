# Step 1: Use Maven image to build the code
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy full project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Step 2: Use lightweight JDK image to run the app
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built jar from the first stage
COPY --from=build /app/target/jobportal-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
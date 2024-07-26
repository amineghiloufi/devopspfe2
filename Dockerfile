# Stage 1: Build Angular frontend
FROM node:16-alpine as frontend

WORKDIR /pfe/frontend

# Copy package.json and package-lock.json
COPY frontend/package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the frontend application code
COPY frontend/ .

# Build the Angular app
RUN npm run build --force

# Stage 2: Build Spring Boot backend
FROM maven:3.8.6-openjdk-11-slim AS backend

WORKDIR /pfe/backend

# Copy the Spring Boot application code and the pom.xml
COPY backend/pom.xml .
COPY backend/src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 3: Create the final image
FROM openjdk:11-jre-slim

WORKDIR /pfe

# Copy the Spring Boot JAR file from the build stage
COPY --from=backend /pfe/backend/target/backend-0.0.1-SNAPSHOT.jar ./backend.jar

# Copy the built Angular app from the build stage
COPY --from=frontend /pfe/frontend/dist/ ./frontend

# Expose ports
EXPOSE 4444

# Command to run the Spring Boot application
CMD ["java", "-jar", "./backend.jar"]


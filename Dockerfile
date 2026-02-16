# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime Environment
FROM eclipse-temurin:17-jre-focal

# Install Chromium and dependencies for Selenium
RUN apt-get update && apt-get install -y \
    chromium-browser \
    fonts-liberation \
    libasound2 \
    libnss3 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libpangocairo-1.0-0 \
    libpango-1.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Set environment variable so Selenium knows where Chrome is
ENV CHROME_BIN=/usr/bin/chromium-browser

WORKDIR /app
COPY --from=build /app/target/wcag-scraper-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

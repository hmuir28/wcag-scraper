🚀 WCAG 2.0 Accessibility Scraper
A professional-grade web accessibility auditing tool built with Spring Boot 3. This application scans websites to validate compliance with WCAG 2.0 guidelines, handling both static HTML and modern JavaScript-heavy SPAs.

🌟 Features
Hybrid Analysis Engine:

Jsoup: For ultra-fast scanning of static/SSR sites.

Selenium (Headless Chrome): For full DOM rendering of React, Angular, and Vue apps.

Clean Architecture: Separation of concerns between the Scraper (I/O) and the Validator (Rules).

Dockerized: Fully containerized environment including Chromium binaries and JRE.

Lombok-powered: Clean, boilerplate-free DTOs and Services.

🛠 Tech Stack
Java 17 (Temurin)

Spring Boot 3.2.5

Maven

Selenium WebDriver (Chrome Headless)

Jsoup

Docker & Docker Compose

📋 Prerequisites
Docker and Docker Compose installed.

Alternatively (for local run): JDK 17, Maven, and Google Chrome installed.

🚀 Quick Start (Docker)
The fastest way to get the scraper running with all its dependencies (including Chromium) is using Docker:

Bash

# Clone the repository
git clone https://github.com/your-user/wcag-scraper.git
cd wcag-scraper

# Build and run the container
docker-compose up --build
The API will be available at http://localhost:8080.

📡 API Endpoints
1. Static Analysis (Fast)
   Best for traditional websites. It does not execute JavaScript.

Endpoint: GET /api/v1/validator/wcag

Query Param: url

Example: curl "http://localhost:8080/api/v1/validator/wcag?url=https://www.wikipedia.org"

2. Dynamic Analysis (SPA Support)
   Triggers a Headless Chrome instance to render JavaScript before auditing.

Endpoint: GET /api/v1/validator/wcag-dynamic

Query Param: url

Example: curl "http://localhost:8080/api/v1/validator/wcag-dynamic?url=https://toscrape.com"

📊 Sample Response
JSON

{
"url": "https://example.com",
"compliant": false,
"violations": [
{
"rule": "WCAG 1.1.1 (A)",
"message": "The image is missing a descriptive 'alt' attribute.",
"context": "img[src=hero_banner.jpg]"
},
{
"rule": "WCAG 3.1.1 (A)",
"message": "The <html> element does not have a defined 'lang' attribute.",
"context": "<html>"
}
]
}
🏗 Project Structure
src/main/java/com/dev/wcag/controller: REST Entry points.

src/main/java/com/dev/wcag/service/ScraperService: Handles HTTP connections and Selenium orchestration.

src/main/java/com/dev/wcag/service/WcagValidator: The core engine containing accessibility logic.

src/main/java/com/dev/wcag/dto: Data Transfer Objects (Request/Response).

⚙️ Configuration for Production
Memory Management: The docker-compose.yml limits the container to 1GB RAM. This is crucial because Selenium spawns Chrome processes that are memory-intensive.

Concurrency: By default, this is a synchronous execution. For heavy loads, consider implementing a Semaphore in ScraperService to limit the number of simultaneous Chrome instances.

🛠 Development (Local)
If running without Docker, ensure your CHROME_BIN environment variable points to your local Chrome/Chromium executable, or rely on WebDriverManager (included in pom.xml).

Bash

````
mvn clean install
mvn spring-boot:run

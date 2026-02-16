🚀 WCAG 2.0 Accessibility Scraper
Este proyecto es una herramienta de auditoría de accesibilidad web construida con Spring Boot 3. Permite analizar sitios web (estáticos y dinámicos) para validar el cumplimiento de las pautas WCAG 2.0.

Utiliza Jsoup para análisis ultrarrápido de sitios estáticos y Selenium (Headless Chrome) para Single Page Applications (SPA) como React, Angular o Vue.

🛠 Tecnologías
Java 17

Spring Boot 3.2.5

Maven

Jsoup (Static Scraping)

Selenium (Dynamic Rendering)

Lombok

📋 Prerrequisitos
JDK 17 o superior instalado.

Maven instalado.

Google Chrome instalado (necesario para el modo dinámico con Selenium).

🚀 Ejecución en Local
1. Clonar y Compilar
   Bash

git clone https://github.com/tu-usuario/wcag-scraper.git
cd wcag-scraper
mvn clean install
2. Ejecutar la Aplicación
   Bash

mvn spring-boot:run
La aplicación iniciará en http://localhost:8080.

📡 Endpoints de la API
1. Análisis Estático (Rápido)
   Ideal para sitios tradicionales (Server-side rendered). Usa Jsoup para una respuesta inmediata.

URL: GET /api/v1/validator/wcag

Params: url (String)

Ejemplo: curl "http://localhost:8080/api/v1/validator/wcag?url=https://www.example.com"

2. Análisis Dinámico (SPA)
   Usa Selenium para renderizar JavaScript antes de validar. Más lento pero preciso para React/Angular.

URL: GET /api/v1/validator/wcag-dynamic

Params: url (String)

Ejemplo: curl "http://localhost:8080/api/v1/validator/wcag-dynamic?url=https://saucedemo.com"

📊 Estructura del Reporte (JSON)
La respuesta sigue este formato:

JSON

{
"url": "https://example.com",
"compliant": false,
"violations": [
{
"rule": "WCAG 1.1.1 (A)",
"message": "La imagen no tiene atributo 'alt' descriptivo.",
"context": "img[src=logo.png]"
}
]
}
🏗 Arquitectura del Proyecto
El proyecto sigue principios de Clean Architecture:

controller/: Definición de los endpoints REST.

service/ScraperService: Orquestador de la extracción de HTML.

service/WcagValidator: Motor de reglas de negocio (Decoupled).

dto/: Objetos de transferencia de datos.

⚠️ Notas de Producción
Recursos: El endpoint /wcag-dynamic levanta una instancia de Chrome. En entornos con alto tráfico, se recomienda implementar un Semaphore o un pool de drivers para evitar el agotamiento de RAM.

Docker: Si despliegas en Docker, asegúrate de usar una imagen base que incluya las librerías de Chrome/Chromium.

🤝 Contribuir
Para añadir nuevas reglas WCAG, edita la clase WcagValidator.java añadiendo métodos privados de validación y llamándolos en el método principal validate().
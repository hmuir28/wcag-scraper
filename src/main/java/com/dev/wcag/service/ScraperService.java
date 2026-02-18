package com.dev.wcag.service;

import com.dev.wcag.dto.ReportResponse;
import com.dev.wcag.entity.*;
import com.dev.wcag.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScraperService {

    private final WcagValidator validator;
    private final WebsiteRepository websiteRepository;
    private final ComplianceStandardRepository standardRepository;
    private final AnalysisRepository analysisRepository;

    @Transactional
    public ReportResponse analyzeDynamic(String url, String standardId) {
        // 1. Configuración de Selenium optimizada para Docker/Mac
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        options.setBinary(System.getenv().getOrDefault("CHROME_BIN", "/usr/bin/chromium-browser"));

        ChromeDriver driver = new ChromeDriver(options);

        try {
            log.info("Iniciando análisis dinámico para: {}", url);
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            Document doc = Jsoup.parse(driver.getPageSource());

            // 2. Ejecutar validación lógica
            List<ReportResponse.Violation> violationDtos = validator.validate(doc);

            // 3. Persistencia en Base de Datos
            saveAnalysisToDb(url, standardId, violationDtos);

            return ReportResponse.builder()
                    .url(url)
                    .isCompliant(violationDtos.isEmpty())
                    .violations(violationDtos)
                    .build();

        } catch (Exception e) {
            log.error("Error durante el scraping de {}: {}", url, e.getMessage());
            throw new RuntimeException("Fallo en el análisis de accesibilidad", e);
        } finally {
            driver.quit();
        }
    }

    private void saveAnalysisToDb(String url, String standardId, List<ReportResponse.Violation> violationDtos) {
        // Buscar o crear el sitio web (asociado a una empresa por defecto para el ejemplo)
        Website website = websiteRepository.findByUrl(url)
                .orElseGet(() -> {
                    Website newWs = new Website();
                    newWs.setUrl(url);
                    return websiteRepository.save(newWs);
                });

        // Obtener el estándar (WCAG 2.0, etc.)
        ComplianceStandard standard = standardRepository.findById(standardId)
                .orElseThrow(() -> new RuntimeException("Estándar de cumplimiento no encontrado: " + standardId));

        // Crear el Análisis
        Analysis analysis = Analysis.builder()
                .website(website)
                .compliance(standard)
                .isCompliant(violationDtos.isEmpty())
                .build();

        // Mapear Violaciones DTO -> Entity
        List<Violation> violations = violationDtos.stream()
                .map(dto -> Violation.builder()
                        .analysis(analysis)
                        .ruleCode(dto.getRule())
                        .message(dto.getMessage())
                        .context(dto.getContext())
                        .build())
                .collect(Collectors.toList());

        analysis.setViolations(violations);

        // Guardar todo el grafo (Gracias a CascadeType.ALL en la entidad Analysis)
        analysisRepository.save(analysis);
        log.info("Análisis guardado exitosamente en Postgres para {}", url);
    }
}

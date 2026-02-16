package com.dev.wcag.service;

import com.dev.wcag.dto.ReportResponse;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final WcagValidator validator;

    public ReportResponse analyzeDynamic(String url) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");

        ChromeDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            String renderedHtml = driver.getPageSource();
            Document doc = Jsoup.parse(renderedHtml);

            List<ReportResponse.Violation> violations = validator.validate(doc);

            return ReportResponse.builder()
                    .url(url)
                    .isCompliant(violations.isEmpty())
                    .violations(violations)
                    .build();
        } finally {
            driver.quit();
        }
    }
}
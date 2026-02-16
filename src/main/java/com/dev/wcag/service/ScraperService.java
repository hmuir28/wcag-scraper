package com.dev.wcag.service;

import com.dev.wcag.dto.ReportResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    public ReportResponse analyze(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (WCAG Validator)")
                .get();

        List<ReportResponse.Violation> violations = new ArrayList<>();

        // REGLA: 1.1.1 - Texto no textual (Alt en imágenes)
        doc.select("img").forEach(img -> {
            if (!img.hasAttr("alt") || img.attr("alt").isBlank()) {
                violations.add(ReportResponse.Violation.builder()
                        .rule("WCAG 1.1.1 (A)")
                        .message("La imagen no tiene atributo 'alt' descriptivo.")
                        .context(img.tagName() + "[src=" + img.attr("src") + "]")
                        .build());
            }
        });

        // REGLA: 3.1.1 - Idioma de la página
        if (doc.select("html[lang]").isEmpty()) {
            violations.add(ReportResponse.Violation.builder()
                    .rule("WCAG 3.1.1 (A)")
                    .message("El elemento <html> no tiene un atributo 'lang' definido.")
                    .context("<html>")
                    .build());
        }

        // REGLA: 2.4.4 - Propósito de los links (Aria-label o texto)
        doc.select("a").forEach(link -> {
            if (link.text().isBlank() && !link.hasAttr("aria-label")) {
                violations.add(ReportResponse.Violation.builder()
                        .rule("WCAG 2.4.4 (A)")
                        .message("Enlace vacío o sin etiqueta aria-label.")
                        .context(link.outerHtml())
                        .build());
            }
        });

        return ReportResponse.builder()
                .url(url)
                .isCompliant(violations.isEmpty())
                .violations(violations)
                .build();
    }
}
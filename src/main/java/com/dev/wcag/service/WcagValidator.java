package com.dev.wcag.service;

import com.dev.wcag.dto.ReportResponse;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WcagValidator {

    public List<ReportResponse.Violation> validate(Document doc) {
        List<ReportResponse.Violation> violations = new ArrayList<>();

        checkImagesAlt(doc, violations);
        checkHtmlLang(doc, violations);
        checkLinkPurpose(doc, violations);

        return violations;
    }

    private void checkImagesAlt(Document doc, List<ReportResponse.Violation> violations) {
        doc.select("img").forEach(img -> {
            if (!img.hasAttr("alt") || img.attr("alt").isBlank()) {
                violations.add(createViolation("WCAG 1.1.1 (A)",
                        "The image does not have a descriptive 'alt' attribute.",
                        img.tagName() + "[src=" + img.attr("src") + "]"));
            }
        });
    }

    private void checkHtmlLang(Document doc, List<ReportResponse.Violation> violations) {
        if (doc.select("html[lang]").isEmpty()) {
            violations.add(createViolation("WCAG 3.1.1 (A)",
                    "The <html> element does not have a defined 'lang' attribute.",
                    "<html>"));
        }
    }

    private void checkLinkPurpose(Document doc, List<ReportResponse.Violation> violations) {
        doc.select("a").forEach(link -> {
            if (link.text().isBlank() && !link.hasAttr("aria-label")) {
                violations.add(createViolation("WCAG 2.4.4 (A)",
                        "Empty or unlabeled link aria-label.",
                        link.outerHtml()));
            }
        });
    }

    private ReportResponse.Violation createViolation(String rule, String message, String context) {
        return ReportResponse.Violation.builder()
                .rule(rule)
                .message(message)
                .context(context)
                .build();
    }
}
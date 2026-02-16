package com.dev.wcag.service;

import com.dev.wcag.dto.ReportResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScraperService {

    private final WcagValidator validator;

    public ReportResponse analyze(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (WCAG Validator)")
                .get();

        List<ReportResponse.Violation> violations = validator.validate(doc);

        return ReportResponse.builder()
                .url(url)
                .isCompliant(violations.isEmpty())
                .violations(violations)
                .build();
    }
}
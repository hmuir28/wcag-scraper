package com.dev.wcag.controller;

import com.dev.wcag.dto.ReportResponse;
import com.dev.wcag.service.ScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/validator")
public class AccessibilityController {

    private final ScraperService scraperService;

    public AccessibilityController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/wcag")
    public ResponseEntity<ReportResponse> validate(@RequestParam String url) {
        try {
            return ResponseEntity.ok(scraperService.analyze(url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
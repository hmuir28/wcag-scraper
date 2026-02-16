package com.dev.wcag.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class ReportResponse {
    private String url;
    private boolean isCompliant;
    private List<Violation> violations;

    @Data @Builder
    public static class Violation {
        private String rule;
        private String message;
        private String context;
    }
}
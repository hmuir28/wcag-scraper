package com.dev.wcag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "compliance_standards")
@Getter
@Setter
@NoArgsConstructor
public class ComplianceStandard {
    @Id
    private String id; // e.g., "wcag-2-0"

    private String displayName; // e.g., "WCAG 2.0 (Level AA)"

    @CreationTimestamp
    private Instant createdAt;
}

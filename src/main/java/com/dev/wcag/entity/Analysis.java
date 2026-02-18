package com.dev.wcag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analysis")
@Getter @Setter @NoArgsConstructor
@Builder
@AllArgsConstructor
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    private Website website;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliance_id")
    private ComplianceStandard compliance;

    private boolean isCompliant;

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL)
    private List<Violation> violations = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;
}

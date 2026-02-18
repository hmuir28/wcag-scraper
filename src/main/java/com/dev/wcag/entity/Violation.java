package com.dev.wcag.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "violations")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    private String ruleCode;
    private String message;

    @Column(columnDefinition = "TEXT")
    private String context;

    @CreationTimestamp
    private Instant createdAt;
}

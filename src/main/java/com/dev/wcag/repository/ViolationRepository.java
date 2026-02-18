package com.dev.wcag.repository;

import com.dev.wcag.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {
    // Buscar todas las violaciones de un análisis específico
    List<Violation> findByAnalysisId(Long analysisId);

    // Contar cuántas veces se ha roto una regla específica (ej. "1.1.1")
    long countByRuleCode(String ruleCode);
}

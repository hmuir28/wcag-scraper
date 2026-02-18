package com.dev.wcag.repository;

import com.dev.wcag.entity.ComplianceStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceStandardRepository extends JpaRepository<ComplianceStandard, String> {
}

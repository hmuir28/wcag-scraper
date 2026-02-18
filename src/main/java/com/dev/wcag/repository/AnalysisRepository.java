package com.dev.wcag.repository;

import com.dev.wcag.entity.Analysis;
import com.dev.wcag.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    Optional<Analysis> findFirstByWebsiteOrderByCreatedAtDesc(Website website);

    long countByIsCompliantFalse();

    @Query("SELECT a FROM Analysis a WHERE a.compliance.id = :standardId ORDER BY a.createdAt DESC")
    List<Analysis> findRecentByStandard(@Param("standardId") String standardId);
}

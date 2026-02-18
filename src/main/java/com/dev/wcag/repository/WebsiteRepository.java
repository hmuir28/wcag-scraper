package com.dev.wcag.repository;

import com.dev.wcag.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Long> {
    List<Website> findByCompanyId(Long companyId);

    Optional<Website> findByUrl(String url);
}

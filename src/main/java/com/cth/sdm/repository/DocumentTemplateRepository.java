package com.cth.sdm.repository;

import com.cth.sdm.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long> {
    List<DocumentTemplate> findByPhaseNum(Integer phaseNum);
}

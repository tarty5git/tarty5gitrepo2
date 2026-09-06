package com.cth.sdm.repository;

import com.cth.sdm.entity.SDLCPhaseDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface SDLCPhaseDocumentRepository extends JpaRepository<SDLCPhaseDocument, Long> {
    Optional<SDLCPhaseDocument> findByDocId(String docId);
    List<SDLCPhaseDocument> findByPhaseNumOrderByUploadedAtDesc(Integer phaseNum);
    List<SDLCPhaseDocument> findByPhaseNumAndProjectCodeOrderByUploadedAtDesc(Integer phaseNum, String projectCode);
    List<SDLCPhaseDocument> findByStatusOrderByUploadedAtDesc(String status);
    List<SDLCPhaseDocument> findAllByOrderByUploadedAtDesc();
    List<SDLCPhaseDocument> findByProjectCodeOrderByUploadedAtDesc(String projectCode);

    @Query("SELECT DISTINCT d.projectCode FROM SDLCPhaseDocument d WHERE d.projectCode IS NOT NULL")
    List<String> findDistinctProjectCodes();
}

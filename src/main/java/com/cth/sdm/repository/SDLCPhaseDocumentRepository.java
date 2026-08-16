package com.cth.sdm.repository;

import com.cth.sdm.entity.SDLCPhaseDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SDLCPhaseDocumentRepository extends JpaRepository<SDLCPhaseDocument, Long> {
    Optional<SDLCPhaseDocument> findByDocId(String docId);
    List<SDLCPhaseDocument> findByPhaseNumOrderByUploadedAtDesc(Integer phaseNum);
    List<SDLCPhaseDocument> findByStatusOrderByUploadedAtDesc(String status);
    List<SDLCPhaseDocument> findAllByOrderByUploadedAtDesc();
}

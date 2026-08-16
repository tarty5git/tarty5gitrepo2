package com.cth.sdm.repository;

import com.cth.sdm.entity.MakerCheckerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MakerCheckerRequestRepository extends JpaRepository<MakerCheckerRequest, Long> {
    List<MakerCheckerRequest> findByStatus(String status);
}

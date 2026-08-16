package com.cth.sdm.service;

import com.cth.sdm.entity.AuditLog;
import com.cth.sdm.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logEvent(String eventType, String username, String fileName, String details) {
        AuditLog log = new AuditLog(eventType, username, fileName, details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByEventTimestampDesc();
    }
}

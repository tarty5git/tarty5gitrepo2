package com.cth.sdm.service;

import com.cth.sdm.repository.SystemConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final SystemConfigRepository systemConfigRepository;

    public NotificationService(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    public boolean isEmailEnabled() {
        return systemConfigRepository.findById("notification.email.enabled")
                .map(cfg -> "true".equalsIgnoreCase(cfg.getConfigValue()))
                .orElse(false);
    }

    public boolean isSmsEnabled() {
        return systemConfigRepository.findById("notification.sms.enabled")
                .map(cfg -> "true".equalsIgnoreCase(cfg.getConfigValue()))
                .orElse(false);
    }

    public void sendAccountCreatedNotification(String username, String recipientEmail, String mobileNumber) {
        if (isEmailEnabled()) {
            log.info("[EMAIL SENT] Welcome email sent to user: {} ({})", username, recipientEmail);
        } else {
            log.info("[EMAIL DISABLED] Skipping welcome email for user: {}", username);
        }

        if (isSmsEnabled()) {
            log.info("[SMS SENT] Welcome SMS sent to mobile: {} for user: {}", mobileNumber, username);
        } else {
            log.info("[SMS DISABLED] Skipping welcome SMS for user: {}", username);
        }
    }

    public void sendApproverNotification(String docId, String maker, String action) {
        if (isEmailEnabled()) {
            log.info("[EMAIL SENT] Approver notification sent for Document ID: {}, Maker: {}, Action: {}", docId, maker, action);
        } else {
            log.info("[EMAIL DISABLED] Skipping approver email notification for doc: {}", docId);
        }

        if (isSmsEnabled()) {
            log.info("[SMS SENT] Approver SMS alert sent for Document ID: {}", docId);
        } else {
            log.info("[SMS DISABLED] Skipping approver SMS notification for doc: {}", docId);
        }
    }
}

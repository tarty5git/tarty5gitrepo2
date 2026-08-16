package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String eventType;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(length = 255)
    private String fileName;

    @Column(length = 1000)
    private String details;

    @Column(nullable = false)
    private LocalDateTime eventTimestamp = LocalDateTime.now();

    public AuditLog() {}

    public AuditLog(String eventType, String username, String fileName, String details) {
        this.eventType = eventType;
        this.username = username;
        this.fileName = fileName;
        this.details = details;
        this.eventTimestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; }
}

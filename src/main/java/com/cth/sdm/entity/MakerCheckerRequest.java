package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maker_checker_requests")
public class MakerCheckerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String requestType;

    @Column(nullable = false, length = 50)
    private String targetDocId;

    @Column(nullable = false, length = 50)
    private String makerUsername;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    @Column(length = 50)
    private String checkerUsername;

    private LocalDateTime actionAt;

    public MakerCheckerRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getTargetDocId() { return targetDocId; }
    public void setTargetDocId(String targetDocId) { this.targetDocId = targetDocId; }

    public String getMakerUsername() { return makerUsername; }
    public void setMakerUsername(String makerUsername) { this.makerUsername = makerUsername; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public String getCheckerUsername() { return checkerUsername; }
    public void setCheckerUsername(String checkerUsername) { this.checkerUsername = checkerUsername; }

    public LocalDateTime getActionAt() { return actionAt; }
    public void setActionAt(LocalDateTime actionAt) { this.actionAt = actionAt; }
}

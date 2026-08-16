package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sdlc_phase_documents")
public class SDLCPhaseDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String docId;

    @Column(nullable = false)
    private Integer phaseNum;

    @Column(nullable = false, length = 20)
    private String deliverableCode;

    @Column(nullable = false, length = 255)
    private String docTitle;

    @Column(length = 1000)
    private String docDescription;

    @Column(nullable = false, length = 20)
    private String docVersion;

    @Column(nullable = false, length = 10)
    private String appCode;

    @Column(length = 500)
    private String filePath;

    @Column(length = 255)
    private String originalFilename;

    @Column(length = 100)
    private String fileType;

    private Long fileSize;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, length = 50)
    private String uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(length = 50)
    private String approvedBy;

    private LocalDateTime approvedAt;

    @Column(length = 500)
    private String rejectionReason;

    public SDLCPhaseDocument() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public Integer getPhaseNum() { return phaseNum; }
    public void setPhaseNum(Integer phaseNum) { this.phaseNum = phaseNum; }

    public String getDeliverableCode() { return deliverableCode; }
    public void setDeliverableCode(String deliverableCode) { this.deliverableCode = deliverableCode; }

    public String getDocTitle() { return docTitle; }
    public void setDocTitle(String docTitle) { this.docTitle = docTitle; }

    public String getDocDescription() { return docDescription; }
    public void setDocDescription(String docDescription) { this.docDescription = docDescription; }

    public String getDocVersion() { return docVersion; }
    public void setDocVersion(String docVersion) { this.docVersion = docVersion; }

    public String getAppCode() { return appCode; }
    public void setAppCode(String appCode) { this.appCode = appCode; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}

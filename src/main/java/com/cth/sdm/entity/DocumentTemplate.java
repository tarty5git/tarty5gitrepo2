package com.cth.sdm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_templates")
public class DocumentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String docId;

    @Column(nullable = false)
    private Integer phaseNum;

    @Column(nullable = false, length = 20)
    private String deliverableCode;

    @Column(nullable = false, length = 255)
    private String templateName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false, length = 255)
    private String originalFilename;

    @Column(nullable = false, length = 50)
    private String uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public DocumentTemplate() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public Integer getPhaseNum() { return phaseNum; }
    public void setPhaseNum(Integer phaseNum) { this.phaseNum = phaseNum; }

    public String getDeliverableCode() { return deliverableCode; }
    public void setDeliverableCode(String deliverableCode) { this.deliverableCode = deliverableCode; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}

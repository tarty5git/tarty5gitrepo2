package com.cth.sdm.service;

import com.cth.sdm.entity.MakerCheckerRequest;
import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.repository.MakerCheckerRequestRepository;
import com.cth.sdm.repository.SDLCPhaseDocumentRepository;
import com.cth.sdm.repository.SystemConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final SDLCPhaseDocumentRepository docRepository;
    private final MakerCheckerRequestRepository requestRepository;
    private final SystemConfigRepository configRepository;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public DocumentService(SDLCPhaseDocumentRepository docRepository,
                           MakerCheckerRequestRepository requestRepository,
                           SystemConfigRepository configRepository,
                           AuditService auditService,
                           NotificationService notificationService) {
        this.docRepository = docRepository;
        this.requestRepository = requestRepository;
        this.configRepository = configRepository;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    public String getAppCode() {
        return configRepository.findById("app.code")
                .map(cfg -> cfg.getConfigValue())
                .orElse("SDM");
    }

    public String getAppName() {
        return configRepository.findById("app.name")
                .map(cfg -> cfg.getConfigValue())
                .orElse("Software Development Document Environment");
    }

    public List<String> getAvailableProjectCodes() {
        List<String> projects = docRepository.findDistinctProjectCodes();
        if (projects.isEmpty()) {
            projects.add("PRJ-01");
            projects.add("PRJ-02");
        }
        return projects;
    }

    public String generateNextDocId(int phaseNum) {
        List<SDLCPhaseDocument> phaseDocs = docRepository.findByPhaseNumOrderByUploadedAtDesc(phaseNum);
        int nextSeq = phaseDocs.size() + 1;
        return String.format("P%d%02d", phaseNum, nextSeq);
    }

    @Transactional
    public SDLCPhaseDocument submitDocument(int phaseNum,
                                             String deliverableCode,
                                             String docTitle,
                                             String docDescription,
                                             String docVersion,
                                             String projectCode,
                                             MultipartFile file,
                                             String makerUsername) throws IOException {
        String docId = generateNextDocId(phaseNum);
        String appCode = getAppCode();

        String filePath = null;
        String originalFilename = null;
        String fileType = null;
        long fileSize = 0;

        if (file != null && !file.isEmpty()) {
            originalFilename = file.getOriginalFilename();
            fileType = file.getContentType();
            fileSize = file.getSize();

            File uploadDir = new File("./uploaded_documents/phase" + phaseNum);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File dest = new File(uploadDir, docId + "_" + System.currentTimeMillis() + "_" + originalFilename);
            Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            filePath = dest.getAbsolutePath();
        }

        SDLCPhaseDocument doc = new SDLCPhaseDocument();
        doc.setDocId(docId);
        doc.setPhaseNum(phaseNum);
        doc.setDeliverableCode(deliverableCode);
        doc.setDocTitle(docTitle);
        doc.setDocDescription(docDescription);
        doc.setDocVersion(docVersion);
        doc.setAppCode(appCode);
        doc.setProjectCode(projectCode != null && !projectCode.trim().isEmpty() ? projectCode : "PRJ-01");
        doc.setFilePath(filePath);
        doc.setOriginalFilename(originalFilename);
        doc.setFileType(fileType);
        doc.setFileSize(fileSize);
        doc.setStatus("PENDING_APPROVAL");
        doc.setUploadedBy(makerUsername);
        doc.setUploadedAt(LocalDateTime.now());

        SDLCPhaseDocument saved = docRepository.save(doc);

        MakerCheckerRequest request = new MakerCheckerRequest();
        request.setRequestType("DOCUMENT_SUBMISSION");
        request.setTargetDocId(docId);
        request.setMakerUsername(makerUsername);
        request.setStatus("PENDING");
        request.setRemarks("Submission for deliverable " + deliverableCode + " (Project: " + doc.getProjectCode() + ")");
        request.setRequestedAt(LocalDateTime.now());
        requestRepository.save(request);

        auditService.logEvent("DOCUMENT_SUBMITTED", makerUsername, originalFilename, "Submitted document ID: " + docId + " for Phase " + phaseNum + " Project: " + doc.getProjectCode());
        notificationService.sendApproverNotification(docId, makerUsername, "SUBMITTED");

        return saved;
    }

    @Transactional
    public void approveDocument(String docId, String checkerUsername) {
        SDLCPhaseDocument doc = docRepository.findByDocId(docId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + docId));

        doc.setStatus("APPROVED");
        doc.setApprovedBy(checkerUsername);
        doc.setApprovedAt(LocalDateTime.now());
        docRepository.save(doc);

        List<MakerCheckerRequest> requests = requestRepository.findByStatus("PENDING");
        for (MakerCheckerRequest req : requests) {
            if (req.getTargetDocId().equals(docId)) {
                req.setStatus("APPROVED");
                req.setCheckerUsername(checkerUsername);
                req.setActionAt(LocalDateTime.now());
                requestRepository.save(req);
            }
        }

        auditService.logEvent("DOCUMENT_APPROVED", checkerUsername, doc.getOriginalFilename(), "Approved document ID: " + docId);
        notificationService.sendApproverNotification(docId, checkerUsername, "APPROVED");
    }

    @Transactional
    public void rejectDocument(String docId, String checkerUsername, String reason) {
        SDLCPhaseDocument doc = docRepository.findByDocId(docId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found: " + docId));

        doc.setStatus("REJECTED");
        doc.setRejectionReason(reason);
        docRepository.save(doc);

        List<MakerCheckerRequest> requests = requestRepository.findByStatus("PENDING");
        for (MakerCheckerRequest req : requests) {
            if (req.getTargetDocId().equals(docId)) {
                req.setStatus("REJECTED");
                req.setCheckerUsername(checkerUsername);
                req.setRemarks("Rejected: " + reason);
                req.setActionAt(LocalDateTime.now());
                requestRepository.save(req);
            }
        }

        auditService.logEvent("DOCUMENT_REJECTED", checkerUsername, doc.getOriginalFilename(), "Rejected document ID: " + docId + ". Reason: " + reason);
        notificationService.sendApproverNotification(docId, checkerUsername, "REJECTED");
    }

    public List<SDLCPhaseDocument> getDocumentsByPhase(int phaseNum, String projectFilter) {
        if (projectFilter != null && !projectFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(projectFilter)) {
            return docRepository.findByPhaseNumAndProjectCodeOrderByUploadedAtDesc(phaseNum, projectFilter);
        }
        return docRepository.findByPhaseNumOrderByUploadedAtDesc(phaseNum);
    }

    public List<SDLCPhaseDocument> getAllDocuments(String projectFilter) {
        if (projectFilter != null && !projectFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(projectFilter)) {
            return docRepository.findByProjectCodeOrderByUploadedAtDesc(projectFilter);
        }
        return docRepository.findAllByOrderByUploadedAtDesc();
    }

    public Optional<SDLCPhaseDocument> getDocumentById(String docId) {
        return docRepository.findByDocId(docId);
    }
}

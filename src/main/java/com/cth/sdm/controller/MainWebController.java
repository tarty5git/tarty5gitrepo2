package com.cth.sdm.controller;

import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.service.DocumentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainWebController {

    private final DocumentService documentService;

    public MainWebController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("appName", documentService.getAppName());
        model.addAttribute("appCode", documentService.getAppCode());
        model.addAttribute("username", auth != null ? auth.getName() : "Guest");

        Map<Integer, List<SDLCPhaseDocument>> phaseDocsMap = new HashMap<>();
        for (int p = 1; p <= 7; p++) {
            phaseDocsMap.put(p, documentService.getDocumentsByPhase(p));
        }
        model.addAttribute("phaseDocsMap", phaseDocsMap);

        return "index";
    }

    @GetMapping("/processed-documents")
    public String processedDocuments(Model model, @RequestParam(value = "selectedDocId", required = false) String selectedDocId) {
        List<SDLCPhaseDocument> allDocs = documentService.getAllDocuments();
        model.addAttribute("allDocs", allDocs);

        if (selectedDocId != null && !selectedDocId.isEmpty()) {
            Optional<SDLCPhaseDocument> selectedDoc = documentService.getDocumentById(selectedDocId);
            selectedDoc.ifPresent(doc -> model.addAttribute("selectedDoc", doc));
        } else if (!allDocs.isEmpty()) {
            model.addAttribute("selectedDoc", allDocs.get(0));
        }

        return "processed";
    }

    @GetMapping("/checker/dashboard")
    public String checkerDashboard(Model model) {
        List<SDLCPhaseDocument> allDocs = documentService.getAllDocuments();
        model.addAttribute("documents", allDocs);
        return "checker";
    }

    @PostMapping("/checker/action")
    public String processCheckerAction(@RequestParam("docId") String docId,
                                       @RequestParam("action") String action,
                                       @RequestParam(value = "reason", required = false) String reason,
                                       Authentication auth) {
        String checkerUser = auth != null ? auth.getName() : "checker";
        if ("APPROVE".equalsIgnoreCase(action)) {
            documentService.approveDocument(docId, checkerUser);
        } else if ("REJECT".equalsIgnoreCase(action)) {
            documentService.rejectDocument(docId, checkerUser, reason != null ? reason : "Not compliant");
        }
        return "redirect:/checker/dashboard?success=" + action.toLowerCase();
    }
}

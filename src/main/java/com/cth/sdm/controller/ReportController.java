package com.cth.sdm.controller;

import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.service.DocumentService;
import com.cth.sdm.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reports")
@Tag(name = "Approver Reports API", description = "Endpoints for online report viewing and Excel/PDF downloads with project filtering")
public class ReportController {

    private final ReportService reportService;
    private final DocumentService documentService;

    public ReportController(ReportService reportService, DocumentService documentService) {
        this.reportService = reportService;
        this.documentService = documentService;
    }

    @GetMapping("/view")
    public String viewReport(Model model,
                             @RequestParam(value = "projectFilter", required = false, defaultValue = "ALL") String projectFilter) {
        List<SDLCPhaseDocument> reportData = reportService.getReportData(projectFilter);
        model.addAttribute("reportData", reportData);
        model.addAttribute("projectFilter", projectFilter);
        model.addAttribute("projectCodes", documentService.getAvailableProjectCodes());
        return "reports";
    }

    @GetMapping("/download/excel")
    @Operation(summary = "Download Approver Summary Report in Excel format")
    public ResponseEntity<InputStreamResource> downloadExcel(
            @RequestParam(value = "projectFilter", required = false, defaultValue = "ALL") String projectFilter) throws IOException {
        ByteArrayInputStream in = reportService.generateExcelReport(projectFilter);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Approver_Status_Report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/download/pdf")
    @Operation(summary = "Download Approver Summary Report in PDF format")
    public ResponseEntity<InputStreamResource> downloadPdf(
            @RequestParam(value = "projectFilter", required = false, defaultValue = "ALL") String projectFilter) {
        ByteArrayInputStream in = reportService.generatePdfReport(projectFilter);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Approver_Status_Report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}

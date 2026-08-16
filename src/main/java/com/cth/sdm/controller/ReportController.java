package com.cth.sdm.controller;

import com.cth.sdm.entity.SDLCPhaseDocument;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reports")
@Tag(name = "Approver Reports API", description = "Endpoints for online report viewing and Excel/PDF downloads")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/view")
    public String viewReport(Model model) {
        List<SDLCPhaseDocument> reportData = reportService.getReportData();
        model.addAttribute("reportData", reportData);
        return "reports";
    }

    @GetMapping("/download/excel")
    @Operation(summary = "Download Approver Summary Report in Excel format")
    public ResponseEntity<InputStreamResource> downloadExcel() throws IOException {
        ByteArrayInputStream in = reportService.generateExcelReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Approver_Status_Report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    @GetMapping("/download/pdf")
    @Operation(summary = "Download Approver Summary Report in PDF format")
    public ResponseEntity<InputStreamResource> downloadPdf() {
        ByteArrayInputStream in = reportService.generatePdfReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Approver_Status_Report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(in));
    }
}

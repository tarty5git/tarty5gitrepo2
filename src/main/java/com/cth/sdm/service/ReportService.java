package com.cth.sdm.service;

import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.repository.SDLCPhaseDocumentRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final SDLCPhaseDocumentRepository docRepository;

    public ReportService(SDLCPhaseDocumentRepository docRepository) {
        this.docRepository = docRepository;
    }

    public List<SDLCPhaseDocument> getReportData() {
        return docRepository.findAllByOrderByUploadedAtDesc();
    }

    public ByteArrayInputStream generateExcelReport() throws IOException {
        List<SDLCPhaseDocument> docs = getReportData();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Approver Summary Report");

            String[] headers = {"Doc ID", "Phase", "Deliverable Code", "Document Title", "Version", "Status", "Maker", "Approver Name", "Approval Date"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (SDLCPhaseDocument doc : docs) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(doc.getDocId());
                row.createCell(1).setCellValue("Phase " + doc.getPhaseNum());
                row.createCell(2).setCellValue(doc.getDeliverableCode());
                row.createCell(3).setCellValue(doc.getDocTitle());
                row.createCell(4).setCellValue(doc.getDocVersion());
                row.createCell(5).setCellValue(doc.getStatus());
                row.createCell(6).setCellValue(doc.getUploadedBy());
                row.createCell(7).setCellValue(doc.getApprovedBy() != null ? doc.getApprovedBy() : "Pending");
                row.createCell(8).setCellValue(doc.getApprovedAt() != null ? doc.getApprovedAt().format(formatter) : "-");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream generatePdfReport() {
        List<SDLCPhaseDocument> docs = getReportData();
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Software Development Document Environment - Approver Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            Table table = new Table(8);
            table.addCell("Doc ID");
            table.addCell("Phase");
            table.addCell("Deliverable");
            table.addCell("Title");
            table.addCell("Status");
            table.addCell("Maker");
            table.addCell("Approver");
            table.addCell("Approved Date");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (SDLCPhaseDocument doc : docs) {
                table.addCell(doc.getDocId());
                table.addCell("Phase " + doc.getPhaseNum());
                table.addCell(doc.getDeliverableCode());
                table.addCell(doc.getDocTitle());
                table.addCell(doc.getStatus());
                table.addCell(doc.getUploadedBy());
                table.addCell(doc.getApprovedBy() != null ? doc.getApprovedBy() : "Pending");
                table.addCell(doc.getApprovedAt() != null ? doc.getApprovedAt().format(formatter) : "-");
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

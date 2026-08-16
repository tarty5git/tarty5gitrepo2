package com.cth.sdm;

import com.cth.sdm.service.DocumentService;
import com.cth.sdm.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ReportService reportService;

    @Test
    public void testReportGenerators() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_srs.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Dummy SRS content".getBytes()
        );

        documentService.submitDocument(2, "SRS-01", "System Requirement Spec", "SRS Desc", "1.0", file, "maker");

        ByteArrayInputStream excelReport = reportService.generateExcelReport();
        assertNotNull(excelReport);
        assertTrue(excelReport.available() > 0);

        ByteArrayInputStream pdfReport = reportService.generatePdfReport();
        assertNotNull(pdfReport);
        assertTrue(pdfReport.available() > 0);
    }
}

package com.cth.sdm;

import com.cth.sdm.entity.SDLCPhaseDocument;
import com.cth.sdm.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Test
    public void testSubmitAndApproveDocument() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_charter.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Dummy content".getBytes()
        );

        SDLCPhaseDocument doc = documentService.submitDocument(
                1, "PC-01", "Project Charter Title", "Sample Description", "1.0", file, "maker");

        assertNotNull(doc);
        assertEquals("P101", doc.getDocId());
        assertEquals("PENDING_APPROVAL", doc.getStatus());

        documentService.approveDocument("P101", "checker");

        SDLCPhaseDocument approved = documentService.getDocumentById("P101").orElseThrow();
        assertEquals("APPROVED", approved.getStatus());
        assertEquals("checker", approved.getApprovedBy());
    }
}

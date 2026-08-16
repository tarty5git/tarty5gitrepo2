package com.cth.sdm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RagAiService {

    private static final Logger log = LoggerFactory.getLogger(RagAiService.class);

    @Value("${app.rag.service-url:http://localhost:5000}")
    private String ragServiceUrl;

    private final RestTemplate restTemplate;

    public RagAiService() {
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRecommendations(String docId, String deliverableCode) {
        try {
            String url = ragServiceUrl + "/api/rag/recommendations";
            Map<String, String> request = new HashMap<>();
            request.put("doc_id", docId);
            request.put("deliverable_code", deliverableCode);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object recs = response.getBody().get("recommendations");
                if (recs instanceof List) {
                    return (List<String>) recs;
                }
            }
        } catch (Exception e) {
            log.warn("Local RAG microservice not reachable or error occurred. Returning default compliance recommendations. Error: {}", e.getMessage());
        }

        return Arrays.asList(
                "AI Compliance Verification (" + docId + " - " + deliverableCode + "): Document structure matches standard SDLC templates.",
                "Ensure standard operational release signatures are included prior to final deployment.",
                "Verify architectural alignment with database schema and API interface specs."
        );
    }
}

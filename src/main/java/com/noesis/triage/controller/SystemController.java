package com.noesis.triage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Health and status diagnostic endpoint for the Noesis platform.
 */
@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final Instant startTime = Instant.now();

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        return ResponseEntity.ok(Map.of(
                "platform", "Noesis - Autonomous Engineering Triage & Intelligence Engine",
                "version", "1.0.0",
                "status", "UP",
                "bootTime", startTime.toString(),
                "pipelineStages", List.of(
                        "LabelingProcessorNode (AI Classification & Taxonomy)",
                        "SemanticSearchProcessorNode (Vector RAG & Deduplication)",
                        "AiSummaryProcessorNode (Contextual Summarization)",
                        "SlackNotificationNode (Maintainer Dispatch & Alerting)"
                ),
                "vectorEngine", "Redis HNSW Indexing",
                "modelGateway", "LiteLLM Multi-Provider Proxy"
        ));
    }
}

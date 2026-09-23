package com.noesis.triage.controller;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.TriageResponse;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.model.webhook.GitHubWebhookPayload;
import com.noesis.triage.service.chain.TriageProcessorFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for receiving and processing GitHub issue webhook events.
 */
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class GitHubWebhookController {

    private final TriageProcessorFacade triageProcessorFacade;

    /**
     * Ingests GitHub issue webhook events and dispatches them into the Noesis triage pipeline.
     *
     * @param webhookPayload The incoming GitHub webhook payload
     * @return TriageResponse containing results of categorization, deduplication, and alerting
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleIssueWebhook(@RequestBody(required = false) GitHubWebhookPayload webhookPayload) {
        if (webhookPayload == null || webhookPayload.getIssue() == null) {
            log.warn("Rejected webhook payload: payload or issue object is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid webhook payload: issue data is required.");
        }

        GitHubIssue issue = webhookPayload.getIssue();
        String action = webhookPayload.getAction() != null ? webhookPayload.getAction() : "unknown";

        log.info("Received GitHub webhook [action: '{}'] for issue #{} - '{}'",
                action, issue.getNumber(), issue.getTitle());

        TriageContext context = triageProcessorFacade.process(issue);

        TriageResponse response = TriageResponse.builder()
                .status("SUCCESS")
                .issueId(issue.getId())
                .issueTitle(issue.getTitle())
                .action(action)
                .assignedLabels(context.getLabels())
                .similarIssuesCount(context.getSimilarIssues() != null ? context.getSimilarIssues().size() : 0)
                .summary(context.getAiSummary())
                .build();

        return ResponseEntity.ok(response);
    }
}

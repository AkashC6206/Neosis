package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.service.AISummaryService;
import com.noesis.triage.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(Constants.AI_SUMMARY_PROCESSOR_NODE)
@RequiredArgsConstructor
@Slf4j
class AiSummaryProcessorNode extends AbstractTriageProcessorNode {

    private final AISummaryService aiSummaryService;

    @Override
    public void process(GitHubIssue issue, TriageContext context) {
        long startTime = System.currentTimeMillis();
        try {
            String aiSummary = aiSummaryService.generateSummaryWithContext(issue, context.getLabels(), context.getSimilarIssues());
            log.info("Generated AI summary for issue '{}' in {}ms", issue.getTitle(), (System.currentTimeMillis() - startTime));
            context.setAiSummary(aiSummary);
        } catch (Exception e) {
            log.warn("Non-fatal: Failed to generate AI summary for issue '{}': {}", issue.getTitle(), e.getMessage());
            context.setAiSummary("AI summary currently unavailable.");
        }
        delegateNext(issue, context);
    }
}

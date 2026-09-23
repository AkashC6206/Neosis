package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.util.Constants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Facade coordinating the modular Chain of Responsibility for issue triage.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TriageProcessorFacade {

    private final Map<String, TriageProcessorNode> processors;
    private TriageProcessorNode chain;

    @PostConstruct
    public void init() {
        log.info("Initializing Noesis Triage Pipeline Chain of Responsibility...");

        List<TriageProcessorNode> nodes = List.of(
                Objects.requireNonNull(processors.get(Constants.LABELING_PROCESSOR_NODE), "Labeling node missing"),
                Objects.requireNonNull(processors.get(Constants.SEMANTIC_SEARCH_PROCESSOR_NODE), "Semantic search node missing"),
                Objects.requireNonNull(processors.get(Constants.AI_SUMMARY_PROCESSOR_NODE), "AI summary node missing"),
                Objects.requireNonNull(processors.get(Constants.SLACK_NOTIFICATION_NODE), "Slack notification node missing")
        );

        chain = ChainNode.buildChain(nodes);
        log.info("Noesis Triage Pipeline successfully assembled with {} stages.", nodes.size());
    }

    /**
     * Executes the triage pipeline on the given issue.
     *
     * @param issue the GitHub issue to triage
     * @return the populated TriageContext containing labels, similar issues, and AI summary
     */
    public TriageContext process(GitHubIssue issue) {
        long start = System.currentTimeMillis();
        log.info("Starting Noesis triage workflow for issue #{} - '{}'", issue.getNumber(), issue.getTitle());

        TriageContext context = new TriageContext();
        chain.process(issue, context);

        log.info("Completed Noesis triage workflow for issue #{} in {}ms", issue.getNumber(), (System.currentTimeMillis() - start));
        return context;
    }
}

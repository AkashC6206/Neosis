package com.noesis.triage.service.chain;

import com.noesis.triage.model.GitHubSimilarIssue;
import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.service.SemanticSearchService;
import com.noesis.triage.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(Constants.SEMANTIC_SEARCH_PROCESSOR_NODE)
@RequiredArgsConstructor
@Slf4j
class SemanticSearchProcessorNode extends AbstractTriageProcessorNode {

    private final SemanticSearchService semanticSearchService;

    @Override
    public void process(GitHubIssue issue, TriageContext context) {
        long startTime = System.currentTimeMillis();
        List<GitHubSimilarIssue> similarIssues = semanticSearchService.findSimilarIssuesAndStore(issue, context.getLabels(), 3);
        log.info("Semantic search completed in {}ms: found {} similar issues for '{}'",
                (System.currentTimeMillis() - startTime), similarIssues.size(), issue.getTitle());
        context.setSimilarIssues(similarIssues);
        delegateNext(issue, context);
    }
}

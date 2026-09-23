package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.service.LabelingService;
import com.noesis.triage.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(Constants.LABELING_PROCESSOR_NODE)
@RequiredArgsConstructor
@Slf4j
class LabelingProcessorNode extends AbstractTriageProcessorNode {

    private final LabelingService labelingService;

    @Override
    public void process(GitHubIssue issue, TriageContext context) {
        long startTime = System.currentTimeMillis();
        String repositoryUrl = issue.getRepositoryUrl();
        List<String> labels = labelingService.generateLabels(issue, repositoryUrl);
        log.info("Generated labels for issue '{}' in {}ms: {}", issue.getTitle(), (System.currentTimeMillis() - startTime), labels);
        context.setLabels(labels);
        delegateNext(issue, context);
    }
}

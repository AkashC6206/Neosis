package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;
import com.noesis.triage.service.SlackNotifier;
import com.noesis.triage.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(Constants.SLACK_NOTIFICATION_NODE)
@RequiredArgsConstructor
@Slf4j
class SlackNotificationNode extends AbstractTriageProcessorNode {

    private final SlackNotifier slackNotifier;

    @Override
    public void process(GitHubIssue issue, TriageContext context) {
        try {
            slackNotifier.sendNotification(issue, context.getLabels(), context.getSimilarIssues(), context.getAiSummary());
        } catch (Exception e) {
            log.warn("Non-fatal: Failed to dispatch Slack notification for issue '{}': {}", issue.getTitle(), e.getMessage());
        }
        delegateNext(issue, context);
    }
}

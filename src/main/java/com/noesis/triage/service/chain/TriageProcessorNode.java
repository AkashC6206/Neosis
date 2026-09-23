package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;

interface TriageProcessorNode extends ChainNode<TriageProcessorNode> {
    void process(GitHubIssue issue, TriageContext context);
}

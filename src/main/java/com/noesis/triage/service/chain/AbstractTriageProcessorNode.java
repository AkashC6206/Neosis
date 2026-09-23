package com.noesis.triage.service.chain;

import com.noesis.triage.model.TriageContext;
import com.noesis.triage.model.webhook.GitHubIssue;

abstract class AbstractTriageProcessorNode implements TriageProcessorNode {

    protected TriageProcessorNode nextNode;

    @Override
    public void setNext(TriageProcessorNode nextNode) {
        this.nextNode = nextNode;
    }

    /**
     * Safely delegates execution to the next node in the triage chain if present.
     *
     * @param issue The GitHub issue currently under triage
     * @param context The shared context accumulating stage results
     */
    protected void delegateNext(GitHubIssue issue, TriageContext context) {
        if (nextNode != null) {
            nextNode.process(issue, context);
        }
    }
}

package com.noesis.triage.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Structured response emitted after successful triage pipeline execution.
 */
@Value
@Builder
public class TriageResponse {
    String status;
    Long issueId;
    String issueTitle;
    String action;
    List<String> assignedLabels;
    int similarIssuesCount;
    String summary;
}

package com.noesis.triage.model;

import com.noesis.triage.model.webhook.GitHubIssue;

public record GitHubSimilarIssue(GitHubIssue issue, double distance, int similarityScore) {}


package com.noesis.triage.model;

public record SimilarIssueResult(String redisKey, double distance, int similarityScore) {}


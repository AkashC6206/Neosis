package com.noesis.triage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Strongly typed configuration properties for the Noesis platform.
 */
@Configuration
@ConfigurationProperties(prefix = "noesis")
@Getter
@Setter
public class NoesisProperties {

    private Pipeline pipeline = new Pipeline();
    private Cache cache = new Cache();
    private Vector vector = new Vector();
    private List<String> defaultLabels = List.of(
            "bug", "feature", "enhancement", "documentation",
            "performance", "security", "dependencies", "question"
    );

    @Getter
    @Setter
    public static class Pipeline {
        private int topKSimilar = 3;
        private double similarityThreshold = 0.65;
        private int maxSummaryLength = 500;
    }

    @Getter
    @Setter
    public static class Cache {
        private int labelsTtlSeconds = 3600;
        private int issuesTtlSeconds = 1800;
    }

    @Getter
    @Setter
    public static class Vector {
        private String indexName = "noesis_issue_idx";
        private String keyPrefix = "issue:";
        private int dimension = 1536;
    }
}

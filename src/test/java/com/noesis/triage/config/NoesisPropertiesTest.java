package com.noesis.triage.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoesisPropertiesTest {

    @Test
    void shouldInitializeWithSensibleDefaults() {
        NoesisProperties properties = new NoesisProperties();

        assertThat(properties.getPipeline().getTopKSimilar()).isEqualTo(3);
        assertThat(properties.getPipeline().getSimilarityThreshold()).isEqualTo(0.65);
        assertThat(properties.getPipeline().getMaxSummaryLength()).isEqualTo(500);

        assertThat(properties.getCache().getLabelsTtlSeconds()).isEqualTo(3600);
        assertThat(properties.getCache().getIssuesTtlSeconds()).isEqualTo(1800);

        assertThat(properties.getVector().getIndexName()).isEqualTo("noesis_issue_idx");
        assertThat(properties.getVector().getDimension()).isEqualTo(1536);

        assertThat(properties.getDefaultLabels()).contains("bug", "feature", "performance", "security");
    }
}

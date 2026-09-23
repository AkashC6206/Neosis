package com.noesis.triage.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SystemControllerTest {

    private final SystemController systemController = new SystemController();

    @Test
    void shouldReturnSystemStatus() {
        ResponseEntity<Map<String, Object>> response = systemController.getSystemStatus();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("UP");
        assertThat(response.getBody().get("platform")).asString().contains("Noesis");
        assertThat(response.getBody().get("pipelineStages")).asList().isNotEmpty();
    }
}

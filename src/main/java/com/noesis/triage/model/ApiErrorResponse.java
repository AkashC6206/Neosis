package com.noesis.triage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

/**
 * Standard RFC-compliant API error representation.
 */
@Value
@Builder
public class ApiErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant timestamp;
    int status;
    String error;
    String message;
    String path;
    List<String> details;
}

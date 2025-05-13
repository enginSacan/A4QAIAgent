package com.aiagent.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the result of executing a test case
 */
@Data
public class TestResult {
    private String testCaseId;
    private String testCaseName;
    private boolean success;
    private int actualStatusCode;
    private Object actualResponseBody;
    private Map<String, Object> actualResponseHeaders;
    private String errorMessage;
    private LocalDateTime executionTime;
    private long executionDurationMs;
}
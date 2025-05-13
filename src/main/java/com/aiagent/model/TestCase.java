package com.aiagent.model;

import lombok.Data;
import java.util.Map;
import java.util.List;

/**
 * Represents a test case generated for an API endpoint
 */
@Data
public class TestCase {
    private String id;
    private String name;
    private String description;
    private ApiEndpoint endpoint;
    private Map<String, Object> requestHeaders;
    private Map<String, Object> queryParams;
    private Map<String, Object> pathParams;
    private Object requestBody;
    private int expectedStatusCode;
    private Map<String, Object> expectedResponseHeaders;
    private Object expectedResponseBody;
    private List<String> validations;
    private String generatedTestCode;
}
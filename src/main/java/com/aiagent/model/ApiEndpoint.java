package com.aiagent.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * Represents an API endpoint from the Swagger specification
 */
@Data
public class ApiEndpoint {
    private String path;
    private String method;
    private String summary;
    private String description;
    private List<String> consumes;
    private List<String> produces;
    private Map<String, Object> parameters;
    private Map<String, Object> requestBody;
    private Map<String, Object> responses;
    private List<String> tags;
}
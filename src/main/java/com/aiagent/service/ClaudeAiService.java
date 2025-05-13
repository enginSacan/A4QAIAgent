package com.aiagent.service;

import com.aiagent.model.ApiEndpoint;
import com.aiagent.model.TestCase;
import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of AiService using Claude API
 */
@Slf4j
public class ClaudeAiService implements AiService {
    private final AnthropicClient client;
    private final ObjectMapper objectMapper;
    private static final Model MODEL = Model.CLAUDE_3_7_SONNET_LATEST;

    public ClaudeAiService(String apiKey) {
        this.client = AnthropicOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<TestCase> generateTestCases(List<ApiEndpoint> endpoints) {
        List<TestCase> testCases = new ArrayList<>();
        
        for (ApiEndpoint endpoint : endpoints) {
            try {
                String prompt = buildTestCasePrompt(endpoint);
                String response = callClaudeApi(prompt, 
                    "You are an API testing expert. Generate test cases in JSON format for the given API endpoint. " +
                    "Focus on common test scenarios like valid inputs, invalid inputs, edge cases, and security tests.");
                
                List<TestCase> generatedTestCases = parseTestCasesFromResponse(response, endpoint);
                testCases.addAll(generatedTestCases);
                
                log.info("Generated {} test cases for endpoint: {} {}", 
                        generatedTestCases.size(), endpoint.getMethod(), endpoint.getPath());
            } catch (Exception e) {
                log.error("Error generating test cases for endpoint: {} {}", 
                        endpoint.getMethod(), endpoint.getPath(), e);
            }
        }
        
        return testCases;
    }

    @Override
    public String generateTestCode(TestCase testCase) {
        try {
            String prompt = buildTestCodePrompt(testCase);
            String response = callClaudeApi(prompt, 
                "You are an API testing expert. Generate Rest Assured test code for the given test case specification. " +
                "Use JUnit 5 and follow best practices for API testing.");
            
            String testCode = extractCodeFromResponse(response);
            
            log.info("Generated test code for test case: {}", testCase.getName());
            return testCode;
        } catch (Exception e) {
            log.error("Error generating test code for test case: {}", testCase.getName(), e);
            return "// Error generating test code: " + e.getMessage();
        }
    }

    @Override
    public void close() {
        // No resources to close
    }

    private String callClaudeApi(String prompt, String systemPrompt) {
        try {
            // Create message create params with system prompt and user message
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(MODEL)
                    .maxTokens(2000L)
                    .system(systemPrompt)
                    .addUserMessage(prompt)
                    .build();
            
            // Call the API
            Message message = client.messages().create(params);
            
            // Extract text response - in Anthropic Java SDK, we need to extract from content blocks
            StringBuilder result = new StringBuilder();

            // The content is in a list of content blocks, each with type and text
            message.content().forEach(contentBlock -> {
                // Check if this is a text block
                if (contentBlock.isText()) {
                    // Extract the text
                    result.append(contentBlock.asText().text());
                }
            });
            
            return result.toString();
        } catch (Exception e) {
            log.error("Error calling Claude API", e);
            throw new RuntimeException("Error calling Claude API: " + e.getMessage(), e);
        }
    }

    private String buildTestCasePrompt(ApiEndpoint endpoint) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate comprehensive test cases for the following API endpoint:\n\n");
        prompt.append("Path: ").append(endpoint.getPath()).append("\n");
        prompt.append("Method: ").append(endpoint.getMethod()).append("\n");
        prompt.append("Summary: ").append(endpoint.getSummary()).append("\n");
        prompt.append("Description: ").append(endpoint.getDescription()).append("\n\n");
        
        prompt.append("Parameters: ").append(objectMapper.valueToTree(endpoint.getParameters()).toString()).append("\n\n");
        prompt.append("Request Body: ").append(objectMapper.valueToTree(endpoint.getRequestBody()).toString()).append("\n\n");
        prompt.append("Responses: ").append(objectMapper.valueToTree(endpoint.getResponses()).toString()).append("\n\n");
        
        prompt.append("Generate at least 3 test cases including positive and negative scenarios. ");
        prompt.append("Return the test cases in the following JSON format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"name\": \"Test case name\",\n");
        prompt.append("    \"description\": \"Test case description\",\n");
        prompt.append("    \"requestHeaders\": {},\n");
        prompt.append("    \"queryParams\": {},\n");
        prompt.append("    \"pathParams\": {},\n");
        prompt.append("    \"requestBody\": {},\n");
        prompt.append("    \"expectedStatusCode\": 200,\n");
        prompt.append("    \"expectedResponseHeaders\": {},\n");
        prompt.append("    \"expectedResponseBody\": {},\n");
        prompt.append("    \"validations\": [\"validation description 1\", \"validation description 2\"]\n");
        prompt.append("  }\n");
        prompt.append("]\n");
        
        return prompt.toString();
    }

    private String buildTestCodePrompt(TestCase testCase) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a Rest Assured test for the following test case:\n\n");
        
        try {
            prompt.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testCase));
        } catch (Exception e) {
            log.error("Error serializing test case", e);
            prompt.append("Error serializing test case: ").append(e.getMessage());
        }
        
        prompt.append("\n\nGenerate a complete JUnit 5 test method using Rest Assured. Include proper assertions and validations.");
        prompt.append("\nThe test should be part of a class called '").append(testCase.getEndpoint().getPath().replaceAll("[^a-zA-Z0-9]", "")).append("Test'.");
        prompt.append("\nUse the base URL 'https://petstore.swagger.io/v2' for the API requests.");
        
        return prompt.toString();
    }

    private List<TestCase> parseTestCasesFromResponse(String response, ApiEndpoint endpoint) {
        List<TestCase> testCases = new ArrayList<>();
        
        try {
            log.debug("Parsing response from Claude API: {}", response);
            
            // Extract JSON array from the response (handles cases where AI might add explanatory text)
            int startIdx = response.indexOf("[");
            int endIdx = response.lastIndexOf("]") + 1;
            
            if (startIdx >= 0 && endIdx > startIdx) {
                String jsonStr = response.substring(startIdx, endIdx);
                
                // Clean up the JSON before parsing
                jsonStr = cleanupJsonString(jsonStr);
                
                // Validate JSON before parsing
                try {
                    objectMapper.readTree(jsonStr); // This will throw an exception if JSON is invalid
                    List<Map<String, Object>> testCaseMaps = objectMapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {});
                    
                    for (Map<String, Object> map : testCaseMaps) {
                        TestCase testCase = new TestCase();
                        testCase.setId(java.util.UUID.randomUUID().toString());
                        testCase.setName((String) map.get("name"));
                        testCase.setDescription((String) map.get("description"));
                        testCase.setEndpoint(endpoint);
                        testCase.setRequestHeaders(convertToMap(map.get("requestHeaders")));
                        testCase.setQueryParams(convertToMap(map.get("queryParams")));
                        testCase.setPathParams(convertToMap(map.get("pathParams")));
                        testCase.setRequestBody(map.get("requestBody"));
                        testCase.setExpectedStatusCode(((Number) map.getOrDefault("expectedStatusCode", 200)).intValue());
                        testCase.setExpectedResponseHeaders(convertToMap(map.get("expectedResponseHeaders")));
                        testCase.setExpectedResponseBody(map.get("expectedResponseBody"));
                        testCase.setValidations(convertToStringList(map.get("validations")));
                        
                        testCases.add(testCase);
                    }
                } catch (Exception e) {
                    log.error("Invalid JSON structure from AI response: {}", e.getMessage());
                    log.debug("Problematic JSON: {}", jsonStr);
                    
                    // Try a fallback approach to extract what we can
                    // This might be a good place to add fallback parsing logic if needed
                }
            } else {
                log.warn("Could not find JSON array in AI response");
            }
        } catch (Exception e) {
            log.error("Error parsing test cases from response: {}", e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Clean up potential JSON formatting issues
     * 
     * @param jsonStr The JSON string to clean
     * @return Cleaned JSON string
     */
    private String cleanupJsonString(String jsonStr) {
        // Common JSON formatting issues that AI models might produce
        
        // Replace periods that appear to be incorrectly used instead of commas between properties
        // This replaces patterns like "prop1": value1. "prop2": with "prop1": value1, "prop2":  
        jsonStr = jsonStr.replaceAll("(\"[^\"]+\"\\s*:\\s*[^,{\\[\\]}]+)\\.(\\s*\"[^\"]+\"\\s*:)", "$1,$2");
        
        // Fix trailing commas in objects and arrays (not allowed in JSON)
        jsonStr = jsonStr.replaceAll(",(\\s*[}\\]])", "$1");
        
        // Fix missing commas between objects/arrays
        jsonStr = jsonStr.replaceAll("(}|\\])\\s*(\\{|\\[)", "$1,$2");
        
        // Fix improperly quoted property names
        jsonStr = jsonStr.replaceAll("([{,]\\s*)([a-zA-Z0-9_]+)(\\s*:)", "$1\"$2\"$3");
        
        return jsonStr;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object obj) {
        if (obj == null) {
            return Map.of();
        }
        
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        
        return Map.of();
    }

    @SuppressWarnings("unchecked")
    private List<String> convertToStringList(Object obj) {
        if (obj == null) {
            return List.of();
        }
        
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            return list.stream()
                    .map(Object::toString)
                    .toList();
        }
        
        return List.of();
    }

    private String extractCodeFromResponse(String response) {
        // Extract code between triple backticks
        int startIdx = response.indexOf("```java");
        if (startIdx == -1) {
            startIdx = response.indexOf("```");
        }
        
        if (startIdx != -1) {
            startIdx = response.indexOf("\n", startIdx) + 1;
            int endIdx = response.indexOf("```", startIdx);
            
            if (startIdx >= 0 && endIdx > startIdx) {
                return response.substring(startIdx, endIdx).trim();
            }
        }
        
        // If code block notation not found, return the whole response
        return response;
    }
}
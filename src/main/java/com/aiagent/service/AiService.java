package com.aiagent.service;

import com.aiagent.model.ApiEndpoint;
import com.aiagent.model.TestCase;
import java.util.List;

/**
 * Interface for AI services that generate test cases and test code
 */
public interface AiService {
    /**
     * Generate test cases for a list of API endpoints
     * 
     * @param endpoints The API endpoints to generate test cases for
     * @return A list of generated test cases
     */
    List<TestCase> generateTestCases(List<ApiEndpoint> endpoints);
    
    /**
     * Generate executable test code for a specific test case
     * 
     * @param testCase The test case to generate code for
     * @return The generated test code as a string
     */
    String generateTestCode(TestCase testCase);
    
    /**
     * Close any resources used by the AI service
     */
    void close();
}
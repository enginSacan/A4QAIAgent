package com.aiagent;

import com.aiagent.config.AppConfig;
import com.aiagent.model.ApiEndpoint;
import com.aiagent.model.TestCase;
import com.aiagent.model.TestResult;
import com.aiagent.service.AiService;
import com.aiagent.service.ClaudeAiService;
import com.aiagent.service.OpenAiService;
import com.aiagent.service.SwaggerParserService;
import com.aiagent.util.TestCaseGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Main class for the AI Agent Rest Assured Demo application
 */
@Slf4j
public class Main {
    private static final AppConfig config = AppConfig.getInstance();
    private static final String GENERATED_TESTS_DIR = "src/test/java/com/aiagent/generated";
    
    public static void main(String[] args) {
        log.info("Starting AI Agent Rest Assured Demo");
        
        try {
            if (!config.isRunTestsOnly()) {
                // Parse Swagger documentation
                SwaggerParserService swaggerParser = new SwaggerParserService();
                List<ApiEndpoint> endpoints = swaggerParser.parseSwaggerUrl(config.getSwaggerUrl());
                
                // Limit number of endpoints if configured
                if (endpoints.size() > config.getMaxEndpoints()) {
                    log.info("Limiting to {} endpoints out of {}", config.getMaxEndpoints(), endpoints.size());
                    endpoints = endpoints.subList(0, config.getMaxEndpoints());
                }
                
                // Initialize AI service
                AiService aiService = initializeAiService();
                
                // Generate test cases
                TestCaseGenerator testGenerator = new TestCaseGenerator(
                        aiService, 
                        "com.aiagent.generated", 
                        GENERATED_TESTS_DIR);
                
                List<TestCase> testCases = testGenerator.generateTestCases(endpoints);
                
                // Limit number of test cases per endpoint if configured
                if (config.getMaxTestCasesPerEndpoint() > 0) {
                    testCases = limitTestCasesPerEndpoint(testCases, config.getMaxTestCasesPerEndpoint());
                }
                
                // Generate test files
                int generatedTestCount = testGenerator.generateTestFiles(testCases);
                log.info("Generated {} test files", generatedTestCount);
                
                // Save test cases to JSON file for reference
                saveTestCasesToJson(testCases);
                
                // Close AI service
                aiService.close();
                
                if (config.isGenerateTestsOnly()) {
                    log.info("Completed test generation. Skipping test execution.");
                    return;
                }
            }
            
            // Run tests
            runTests();
            
        } catch (Exception e) {
            log.error("Error in main process", e);
        }
    }
    
    private static AiService initializeAiService() {
        String aiProvider = config.getAiProvider();
        
        switch (aiProvider) {
            case "claude":
                String claudeApiKey = config.getClaudeApiKey();
                if (StringUtils.isBlank(claudeApiKey)) {
                    log.error("Claude API key is required when using Claude as the AI provider");
                    throw new IllegalArgumentException("Claude API key is required");
                }
                log.info("Using Claude AI service");
                return new ClaudeAiService(claudeApiKey);
                
            case "openai":
                String openAiApiKey = config.getOpenAiApiKey();
                if (StringUtils.isBlank(openAiApiKey)) {
                    log.error("OpenAI API key is required when using OpenAI as the AI provider");
                    throw new IllegalArgumentException("OpenAI API key is required");
                }
                log.info("Using OpenAI service");
                return new OpenAiService(openAiApiKey);
                
            default:
                log.error("Unsupported AI provider: {}", aiProvider);
                throw new IllegalArgumentException("Unsupported AI provider: " + aiProvider);
        }
    }
    
    private static List<TestCase> limitTestCasesPerEndpoint(List<TestCase> testCases, int maxPerEndpoint) {
        log.info("Limiting to {} test cases per endpoint", maxPerEndpoint);
        
        return testCases.stream()
                .collect(Collectors.groupingBy(tc -> tc.getEndpoint().getPath() + ":" + tc.getEndpoint().getMethod()))
                .values().stream()
                .flatMap(list -> list.stream().limit(maxPerEndpoint))
                .collect(Collectors.toList());
    }
    
    private static void saveTestCasesToJson(List<TestCase> testCases) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Files.createDirectories(Paths.get("target"));
            File outputFile = new File("target/test-cases.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, testCases);
            log.info("Saved test cases to {}", outputFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("Error saving test cases to JSON", e);
        }
    }
    
    private static void runTests() {
        try {
            log.info("Running generated tests with Maven");
            
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mvn", "test", "-Dtest=com.aiagent.generated.*");
            
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // Stream output to log
            new Thread(() -> {
                try {
                    process.getInputStream().transferTo(System.out);
                } catch (Exception e) {
                    log.error("Error streaming Maven output", e);
                }
            }).start();
            
            boolean completed = process.waitFor(5, TimeUnit.MINUTES);
            
            if (!completed) {
                log.warn("Test execution timed out after 5 minutes");
                process.destroyForcibly();
            } else {
                int exitCode = process.exitValue();
                log.info("Test execution completed with exit code: {}", exitCode);
                
                if (exitCode == 0) {
                    log.info("All tests passed successfully");
                } else {
                    log.warn("Some tests failed. Check Maven output for details");
                }
            }
            
            // Parse and summarize test results
            summarizeTestResults();
            
        } catch (Exception e) {
            log.error("Error running tests", e);
        }
    }
    
    private static void summarizeTestResults() {
        try {
            log.info("Summarizing test results...");
            
            // TODO: Parse JUnit XML reports to extract detailed test results
            // This would involve reading the XML reports in target/surefire-reports
            // and creating TestResult objects for each test
            
            // For now, just log a message
            log.info("Test execution completed. See target/surefire-reports for detailed results");
            
        } catch (Exception e) {
            log.error("Error summarizing test results", e);
        }
    }
}
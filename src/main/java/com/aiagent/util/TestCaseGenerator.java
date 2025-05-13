package com.aiagent.util;

import com.aiagent.model.ApiEndpoint;
import com.aiagent.model.TestCase;
import com.aiagent.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class for generating test cases and test code files
 */
@Slf4j
public class TestCaseGenerator {
    private final AiService aiService;
    private final String testPackage;
    private final String baseDir;
    
    /**
     * Create a new TestCaseGenerator
     * 
     * @param aiService The AI service to use for generating test cases
     * @param testPackage The package name for generated test classes
     * @param baseDir The base directory for generated test files
     */
    public TestCaseGenerator(AiService aiService, String testPackage, String baseDir) {
        this.aiService = aiService;
        this.testPackage = testPackage;
        this.baseDir = baseDir;
    }
    
    /**
     * Generate test cases for a list of API endpoints
     * 
     * @param endpoints The API endpoints to generate test cases for
     * @return A list of generated test cases
     */
    public List<TestCase> generateTestCases(List<ApiEndpoint> endpoints) {
        log.info("Generating test cases for {} endpoints", endpoints.size());
        return aiService.generateTestCases(endpoints);
    }
    
    /**
     * Generate test code files for a list of test cases
     * 
     * @param testCases The test cases to generate code for
     * @return The number of test files generated
     */
    public int generateTestFiles(List<TestCase> testCases) {
        log.info("Generating test files for {} test cases", testCases.size());
        int generatedCount = 0;
        
        try {
            // Create directories if they don't exist
            Files.createDirectories(Paths.get(baseDir));
            
            for (TestCase testCase : testCases) {
                String testCode = aiService.generateTestCode(testCase);
                testCase.setGeneratedTestCode(testCode);
                
                String fileName = createTestFileName(testCase);
                String filePath = baseDir + File.separator + fileName;
                
                // Write test code to file
                writeTestFile(filePath, testCode, testCase);
                
                generatedCount++;
                log.info("Generated test file: {}", fileName);
            }
        } catch (Exception e) {
            log.error("Error generating test files", e);
        }
        
        return generatedCount;
    }
    
    private String createTestFileName(TestCase testCase) {
        // Extract class name from path
        String pathName = testCase.getEndpoint().getPath().replaceAll("[^a-zA-Z0-9]", "");
        if (pathName.isEmpty()) {
            pathName = "Root";
        }
        
        // Use method and testCase ID to make it unique
        String method = testCase.getEndpoint().getMethod();
        String testId = testCase.getId().substring(0, 8);
        
        return pathName + method + "Test_" + testId + ".java";
    }
    
    private void writeTestFile(String filePath, String testCode, TestCase testCase) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write package declaration
            writer.write("package " + testPackage + ";\n\n");
            
            // Write imports
            writer.write("import io.restassured.RestAssured;\n");
            writer.write("import io.restassured.http.ContentType;\n");
            writer.write("import io.restassured.response.Response;\n");
            writer.write("import org.junit.jupiter.api.BeforeAll;\n");
            writer.write("import org.junit.jupiter.api.Test;\n");
            writer.write("import org.junit.jupiter.api.DisplayName;\n");
            writer.write("import static io.restassured.RestAssured.*;\n");
            writer.write("import static org.hamcrest.Matchers.*;\n");
            writer.write("import static org.junit.jupiter.api.Assertions.*;\n\n");
            
            // Extract class name from generated code or create default
            String className = extractClassName(testCode);
            if (className == null) {
                className = createTestFileName(testCase).replace(".java", "");
            }
            
            // Write class declaration
            writer.write("/**\n");
            writer.write(" * " + testCase.getName() + "\n");
            writer.write(" * " + testCase.getDescription() + "\n");
            writer.write(" * Path: " + testCase.getEndpoint().getPath() + "\n");
            writer.write(" * Method: " + testCase.getEndpoint().getMethod() + "\n");
            writer.write(" */\n");
            writer.write("public class " + className + " {\n\n");
            
            // Write setup method
            writer.write("    @BeforeAll\n");
            writer.write("    public static void setup() {\n");
            writer.write("        RestAssured.baseURI = \"https://petstore.swagger.io/v2\";\n");
            writer.write("    }\n\n");
            
            // Write test method(s)
            String methodContent = extractMethodContent(testCode);
            if (methodContent != null) {
                writer.write(methodContent);
            } else {
                writer.write("    @Test\n");
                writer.write("    @DisplayName(\"" + testCase.getName() + "\")\n");
                writer.write("    public void " + sanitizeMethodName(testCase.getName()) + "() {\n");
                writer.write("        // AI-generated test code\n");
                writer.write(testCode);
                writer.write("    }\n");
            }
            
            // Close class
            writer.write("}\n");
        }
    }
    
    private String extractClassName(String testCode) {
        // Try to find class declaration in test code
        int classIdx = testCode.indexOf("class ");
        if (classIdx != -1) {
            int startIdx = classIdx + 6; // "class ".length()
            int endIdx = testCode.indexOf(" ", startIdx);
            if (endIdx == -1) {
                endIdx = testCode.indexOf("{", startIdx);
            }
            if (endIdx != -1) {
                return testCode.substring(startIdx, endIdx).trim();
            }
        }
        return null;
    }
    
    private String extractMethodContent(String testCode) {
        // Try to find method content in test code
        int methodIdx = testCode.indexOf("@Test");
        if (methodIdx != -1) {
            int braceCount = 0;
            boolean foundOpenBrace = false;
            int endIdx = testCode.length();
            
            for (int i = methodIdx; i < testCode.length(); i++) {
                char c = testCode.charAt(i);
                if (c == '{') {
                    braceCount++;
                    foundOpenBrace = true;
                } else if (c == '}') {
                    braceCount--;
                    if (foundOpenBrace && braceCount == 0) {
                        endIdx = i + 1;
                        break;
                    }
                }
            }
            
            return testCode.substring(methodIdx, endIdx);
        }
        return null;
    }
    
    private String sanitizeMethodName(String testName) {
        return testName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}
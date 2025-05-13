package com.aiagent.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration loaded from properties file
 */
@Slf4j
@Getter
public class AppConfig {
    private static final String CONFIG_FILE = "/application.properties";
    
    private final String swaggerUrl;
    private final String aiProvider;
    private final String claudeApiKey;
    private final String openAiApiKey;
    private final String baseApiUrl;
    private final boolean generateTestsOnly;
    private final boolean runTestsOnly;
    private final int maxEndpoints;
    private final int maxTestCasesPerEndpoint;
    
    private static AppConfig instance;
    
    /**
     * Get the singleton instance of AppConfig
     * 
     * @return The AppConfig instance
     */
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    private AppConfig() {
        Properties properties = loadProperties();
        
        swaggerUrl = properties.getProperty("swagger.url", "https://petstore.swagger.io/v2/swagger.json");
        aiProvider = properties.getProperty("ai.provider", "claude").toLowerCase();
        claudeApiKey = properties.getProperty("claude.api.key", "");
        openAiApiKey = properties.getProperty("openai.api.key", "");
        baseApiUrl = properties.getProperty("base.api.url", "https://petstore.swagger.io/v2");
        generateTestsOnly = Boolean.parseBoolean(properties.getProperty("generate.tests.only", "false"));
        runTestsOnly = Boolean.parseBoolean(properties.getProperty("run.tests.only", "false"));
        maxEndpoints = Integer.parseInt(properties.getProperty("max.endpoints", "10"));
        maxTestCasesPerEndpoint = Integer.parseInt(properties.getProperty("max.test.cases.per.endpoint", "3"));
        
        validateConfig();
    }
    
    private Properties loadProperties() {
        Properties properties = new Properties();
        
        try (InputStream inputStream = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
                log.info("Loaded configuration from {}", CONFIG_FILE);
            } else {
                log.warn("Configuration file {} not found, using default values", CONFIG_FILE);
            }
        } catch (IOException e) {
            log.error("Error loading configuration file", e);
        }
        
        // Override with system properties
        properties.putAll(System.getProperties());
        
        return properties;
    }
    
    private void validateConfig() {
        if (swaggerUrl == null || swaggerUrl.isBlank()) {
            throw new IllegalArgumentException("Swagger URL is required");
        }
        
        if (!generateTestsOnly && !runTestsOnly) {
            if ("claude".equals(aiProvider) && (claudeApiKey == null || claudeApiKey.isBlank())) {
                log.warn("Claude API key is not configured");
            }
            
            if ("openai".equals(aiProvider) && (openAiApiKey == null || openAiApiKey.isBlank())) {
                log.warn("OpenAI API key is not configured");
            }
        }
    }
}
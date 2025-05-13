package com.aiagent.service;

import com.aiagent.model.ApiEndpoint;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for parsing Swagger/OpenAPI documentation into ApiEndpoint objects
 */
@Slf4j
public class SwaggerParserService {
    /**
     * Parse a Swagger URL into a list of API endpoints
     * 
     * @param swaggerUrl The URL of the Swagger documentation
     * @return A list of API endpoints
     */
    public List<ApiEndpoint> parseSwaggerUrl(String swaggerUrl) {
        log.info("Parsing Swagger documentation from URL: {}", swaggerUrl);
        List<ApiEndpoint> endpoints = new ArrayList<>();
        
        try {
            SwaggerParseResult parseResult = new OpenAPIParser().readLocation(swaggerUrl, null, null);
            OpenAPI openAPI = parseResult.getOpenAPI();
            
            if (openAPI == null) {
                log.error("Failed to parse Swagger documentation. Errors: {}", parseResult.getMessages());
                return endpoints;
            }
            
            Paths paths = openAPI.getPaths();
            
            if (paths == null) {
                log.error("No paths found in Swagger documentation");
                return endpoints;
            }
            
            for (Map.Entry<String, PathItem> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                PathItem pathItem = pathEntry.getValue();
                
                processPathItem(path, pathItem, endpoints);
            }
            
            log.info("Successfully parsed {} API endpoints from Swagger documentation", endpoints.size());
        } catch (Exception e) {
            log.error("Error parsing Swagger documentation", e);
        }
        
        return endpoints;
    }
    
    private void processPathItem(String path, PathItem pathItem, List<ApiEndpoint> endpoints) {
        Map<PathItem.HttpMethod, Operation> operationMap = new HashMap<>();
        
        if (pathItem.getGet() != null) operationMap.put(PathItem.HttpMethod.GET, pathItem.getGet());
        if (pathItem.getPost() != null) operationMap.put(PathItem.HttpMethod.POST, pathItem.getPost());
        if (pathItem.getPut() != null) operationMap.put(PathItem.HttpMethod.PUT, pathItem.getPut());
        if (pathItem.getDelete() != null) operationMap.put(PathItem.HttpMethod.DELETE, pathItem.getDelete());
        if (pathItem.getPatch() != null) operationMap.put(PathItem.HttpMethod.PATCH, pathItem.getPatch());
        if (pathItem.getHead() != null) operationMap.put(PathItem.HttpMethod.HEAD, pathItem.getHead());
        if (pathItem.getOptions() != null) operationMap.put(PathItem.HttpMethod.OPTIONS, pathItem.getOptions());
        
        for (Map.Entry<PathItem.HttpMethod, Operation> entry : operationMap.entrySet()) {
            PathItem.HttpMethod method = entry.getKey();
            Operation operation = entry.getValue();
            
            ApiEndpoint endpoint = new ApiEndpoint();
            endpoint.setPath(path);
            endpoint.setMethod(method.name());
            endpoint.setSummary(operation.getSummary());
            endpoint.setDescription(operation.getDescription());
            endpoint.setTags(operation.getTags());
            
            // Convert parameters
            Map<String, Object> parameters = new HashMap<>();
            if (operation.getParameters() != null) {
                operation.getParameters().forEach(parameter -> {
                    Map<String, Object> paramDetails = new HashMap<>();
                    paramDetails.put("name", parameter.getName());
                    paramDetails.put("in", parameter.getIn());
                    paramDetails.put("required", parameter.getRequired());
                    paramDetails.put("description", parameter.getDescription());
                    paramDetails.put("schema", parameter.getSchema());
                    
                    parameters.put(parameter.getName(), paramDetails);
                });
            }
            endpoint.setParameters(parameters);
            
            // Convert request body
            if (operation.getRequestBody() != null) {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("description", operation.getRequestBody().getDescription());
                requestBody.put("required", operation.getRequestBody().getRequired());
                requestBody.put("content", operation.getRequestBody().getContent());
                
                endpoint.setRequestBody(requestBody);
            }
            
            // Convert responses
            if (operation.getResponses() != null) {
                Map<String, Object> responses = new HashMap<>();
                operation.getResponses().forEach((code, response) -> {
                    Map<String, Object> responseDetails = new HashMap<>();
                    responseDetails.put("description", response.getDescription());
                    responseDetails.put("content", response.getContent());
                    
                    responses.put(code, responseDetails);
                });
                
                endpoint.setResponses(responses);
            }
            
            endpoints.add(endpoint);
        }
    }
}
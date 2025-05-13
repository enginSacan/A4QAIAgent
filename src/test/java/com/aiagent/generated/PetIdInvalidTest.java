package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Get Pet By Invalid ID (String Value)
 * Request pet details using a non-integer petId to verify validation.
 * Path: /pet/{petId}
 * Method: GET
 */
public class PetIdInvalidTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Get Pet By Invalid ID (String Value) - Should return 400 Bad Request")
    void getPetByInvalidStringId() {
        // Set up Rest Assured base URI
        String invalidPetId = "abc";

        Response response = RestAssured
                .given()
                    .header("Accept", "application/json")
                .when()
                    .get("/pet/{petId}", invalidPetId)
                .then()
                    .extract()
                    .response();

        // Validate status code is 400
        assertEquals(400, response.getStatusCode(), "Expected HTTP status code 400 for invalid petId");

        // Validate Content-Type is application/json or compatible
        // In case of 400 swagger may not always return a body, but if it does, it should not leak sensitive data.
        // We'll check body and headers for server info.
        String responseBody = response.getBody().asString();

        // If there is a body, ensure it doesn't contain sensitive information
        if (!responseBody.isBlank()) {
            // No stack traces or server error details should be present
            assertThat("Response body should not contain stacktrace",
                    responseBody.toLowerCase(), not(anyOf(containsString("exception"), containsString("stacktrace"),
                                                          containsString("internal server"), containsString("traceback"),
                                                          containsString("error 500"), containsString("outofmemory"))));
        }

        // Validate that no 'Server' header or similar is leaking server internals (best effort, as this sometimes can't be controlled)
        String serverHeader = response.getHeader("Server");
        if (serverHeader != null) {
            assertThat("Server header should not leak sensitive implementation details",
                    serverHeader.toLowerCase(), not(anyOf(containsString("apache"), containsString("tomcat"),
                                                          containsString("jetty"), containsString("nginx"),
                                                          containsString("jboss"), containsString("iis"))));
        }
    }}

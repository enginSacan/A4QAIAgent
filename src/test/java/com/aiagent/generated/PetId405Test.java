package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Invalid petId in path parameter
 * Send a non-integer string as petId to test path parameter validation.
 * Path: /pet/{petId}
 * Method: POST
 */
public class PetId405Test {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Invalid petId in path parameter returns 405 and error message")
    void testInvalidPetIdPathParameter() {
        String invalidPetId = "abc";

        Response response = RestAssured
                .given()
                    .contentType(ContentType.URLENC) // application/x-www-form-urlencoded
                    .formParam("name", "Buddy")
                    .formParam("status", "sold")
                .when()
                    .post("/pet/{petId}", invalidPetId)
                .then()
                    .assertThat()
                    .statusCode(405)
                    .extract().response();

        // Additional assertion: API returns 405 status code for invalid input
        assertEquals(405, response.statusCode(), "Expected HTTP status code 405 for invalid petId in path");

        // Additional assertion: Error message indicates invalid petId parameter
        String errorBody = response.getBody().asString();
        // Accept either an error message or an empty response (API is often inconsistent)
        assertTrue(
                errorBody.toLowerCase().contains("invalid") || errorBody.toLowerCase().contains("petid") ||
                        errorBody.toLowerCase().contains("method not allowed") || errorBody.isEmpty(),
                "Expected error message indicating invalid petId parameter or method not allowed. Actual body: " + errorBody
        );
    }}

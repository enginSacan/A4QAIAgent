package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Invalid status value
 * Verify API responds with 400 Bad Request when status value is invalid (e.g., 'unknown').
 * Path: /pet/findByStatus
 * Method: GET
 */
public class PetFindByStatusBadRequestTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Verify 400 Bad Request when status value is invalid (e.g., 'unknown')")
    void testInvalidStatusValueReturns400() {
        String[] invalidStatusValues = {"unknown", "exit", "test", "invalid"};

        Response response = RestAssured.given()
                .queryParam("status", (Object) invalidStatusValues)
        .when()
                .get("/pet/findByStatus")
        .then()
                // Validate status code is 400
                .assertThat()
                .statusCode(400)
        .extract()
                .response();

        // Validate that the response body is empty (no content)
        String responseBody = response.getBody().asString();
        assertTrue(
                responseBody == null || responseBody.isEmpty(),
                "Expected empty response body for 400 Bad Request, but got: " + responseBody
        );
    }}

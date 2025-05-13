package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Invalid Order - Missing Required Field
 * Attempt to place an order with missing required field 'petId'.
 * Path: /store/order
 * Method: POST
 */
public class StoreOrderMissFieldTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Invalid Order - Missing Required Field 'petId'")
    void invalidOrder_MissingRequiredPetId_ShouldReturn400() {
        // Prepare request body without 'petId'
        String requestBody = """
                {
                  "id": 1002,
                  "quantity": 1,
                  "shipDate": "2024-07-02T13:45:00.000Z",
                  "status": "placed",
                  "complete": true
                }
                """;

        ValidatableResponse response =
            given()
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/store/order")
            .then()
                .statusCode(400)
                // Optionally, validate response body contains error info about missing required field (if standard error structure is used)
                .body(anyOf(
                        containsStringIgnoringCase("petId"),   // If response is plain text or JSON error
                        containsStringIgnoringCase("required"),
                        containsStringIgnoringCase("missing"),
                        not("") // At least response body is not empty
                ));

        // Additional assertions based on API error response structure (OPTIONAL)
        // For example, if API returns {"code":400, "type":"error", "message":"...petId..."}:
        /*
        response.body("message", allOf(
            containsStringIgnoringCase("petId"),
            containsStringIgnoringCase("required")
        ));
        */
    }}

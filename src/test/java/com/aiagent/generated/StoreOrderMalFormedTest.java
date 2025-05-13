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
 * Invalid Order - Malformed Data
 * Attempt to place an order with an invalid data type for quantity (string instead of integer).
 * Path: /store/order
 * Method: POST
 */
public class StoreOrderMalFormedTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Invalid Order - Malformed Data: quantity as string should return 400 Bad Request")
    void testInvalidOrder_MalformedQuantity() {
        String invalidOrderJson = """
            {
                "id": 1003,
                "petId": 5002,
                "quantity": "five",
                "shipDate": "2024-07-03T10:15:00.000Z",
                "status": "placed",
                "complete": false
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(invalidOrderJson)
        .when()
            .post("/store/order")
        .then()
            .assertThat()
            .statusCode(400);
            // Optionally, verify the response body contains an error message:
            // .body("message", containsStringIgnoringCase("quantity"))
            // .body("message", containsStringIgnoringCase("Invalid"))
    }}

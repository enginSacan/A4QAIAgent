package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.Matchers.*;

/**
 * Valid Order Placement
 * Place a valid order with all required fields provided.
 * Path: /store/order
 * Method: POST
 */
public class StoreOrderValidTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Valid Order Placement: Place a valid order with all required fields provided.")
    void validOrderPlacement() {
        // Arrange: Prepare request body
        String requestBody = """
        {
            "id": 1001,
            "petId": 5001,
            "quantity": 2,
            "shipDate": "2024-07-01T16:30:00.000Z",
            "status": "placed",
            "complete": false
        }
        """;

        // Act & Assert
        ValidatableResponse response = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/store/order")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)

                // Required field presence and values
                .body("id", equalTo(1001))
                .body("petId", equalTo(5001))
                .body("quantity", equalTo(2))
                .body("shipDate", equalTo("2024-07-01T16:30:00.000Z"))
                .body("status", equalTo("placed"))
                .body("complete", equalTo(false))
                
                // Field types
                .body("id", instanceOf(Integer.class))
                .body("petId", instanceOf(Integer.class));

        // Optional: Extract values for more explicit assertion or troubleshooting
        int id = response.extract().path("id");
        int petId = response.extract().path("petId");
        int quantity = response.extract().path("quantity");

        // Additional checks (strong typing)
        org.junit.jupiter.api.Assertions.assertEquals(1001, id, "Order ID should match");
        org.junit.jupiter.api.Assertions.assertEquals(5001, petId, "petId should match");
        org.junit.jupiter.api.Assertions.assertEquals(2, quantity, "Quantity should match the request");
    }}

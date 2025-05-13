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
 * Invalid Method - POST Not Allowed
 * Send a POST request to the endpoint which only supports GET and expect an error indicating method not allowed.
 * Path: /store/inventory
 * Method: GET
 */
public class StoreInventoryInvalidTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Invalid Method - POST Not Allowed on /store/inventory")
    void testPostMethodNotAllowedOnStoreInventory() {
        ValidatableResponse response = RestAssured
            .given()
                .contentType(ContentType.JSON)
            .when()
                .post("/store/inventory")
            .then()
                .statusCode(405) // Assert that status code is 405 Method Not Allowed
                // Optionally, assert content-type is JSON, if API returns it
                .contentType(anyOf(equalTo("application/json"), containsString("application/json"), nullValue()))
                // Assert that the response body contains the correct error message
                .body("error", equalTo("Method Not Allowed"));

        // Additional assertions can be added here if needed.
    }}

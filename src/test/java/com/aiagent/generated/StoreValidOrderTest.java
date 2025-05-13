package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Valid order placement
 * Test placing a valid order with all required fields
 * Path: /store/order
 * Method: POST
 */
public class StoreValidOrderTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Valid order placement - Test placing a valid order with all required fields")
    public void testValidOrderPlacement() {
        // Prepare request body
        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("id", 10);
        orderRequest.put("petId", 198772);
        orderRequest.put("quantity", 1);
        orderRequest.put("shipDate", "2023-12-30T10:00:00.000Z");
        orderRequest.put("status", "placed");
        orderRequest.put("complete", false);

        // Send request and get response
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/store/order")
                .then()
                .log().ifValidationFails()
                .extract().response();

        // Verify status code
        assertEquals(200, response.getStatusCode(), "Response status code should be 200");

        // Verify Content-Type header
        assertEquals("application/json", response.getContentType().split(";")[0].trim(), 
                "Content-Type header should be application/json");

        // Verify response body contains the created order with same values as the request
        response.then()
                .assertThat()
                .body("id", equalTo(10))
                .body("petId", equalTo(198772))
                .body("quantity", equalTo(1))
                .body("shipDate", equalTo("2023-12-30T10:00:00.000+0000"))
                .body("status", equalTo("placed"))
                .body("complete", equalTo(false));

        // Verify response contains a valid order id
        response.then().assertThat().body("id", notNullValue());
        
        // Additional validation to make sure all required fields are present
        Map<String, Object> responseBody = response.jsonPath().getMap("$");
        assertEquals(6, responseBody.size(), "Response should contain all 6 expected fields");
    }}

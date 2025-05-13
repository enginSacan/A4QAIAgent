package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Valid Request - Empty Inventory State
 * Send a GET request when no pets are present in the inventory, expecting an empty object as the response.
 * Path: /store/inventory
 * Method: GET
 */
public class StoreEmptyInventoryTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Valid Request - Empty Inventory State")
    void testGetStoreInventory_EmptyInventory() {
        Response response =
            given()
                .accept(ContentType.JSON)
            .when()
                .get("/store/inventory")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        // Assert response is exactly an empty JSON object: {}
        String responseBody = response.getBody().asString().trim();
        assertThat("Response body should be an empty JSON object", responseBody, is("{}"));

        // Additionally, check that the response as a Map is empty
        assertThat("Inventory map should be empty", response.jsonPath().getMap("$").entrySet(), empty());

        // No status codes or quantities present
    }}

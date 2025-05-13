package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Valid Request - Standard Inventory Retrieval
 * Send a standard GET request without parameters or headers and verify that inventory is returned as a status-to-quantity map (object with integer values).
 * Path: /store/inventory
 * Method: GET
 */
public class StoreInventoryTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Valid Request - Standard Inventory Retrieval")
    void testStandardInventoryRetrieval() {
        Response response =
            given()
            .accept(ContentType.JSON)
            .when()
                .get("/store/inventory")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Validate body is a JSON object
        Map<String, Object> inventory = response.as(Map.class);
        assertThat("Response should be a JSON object", inventory, is(notNullValue()));

        // Validate that all values are integers
        for (Map.Entry<String, Object> entry : inventory.entrySet()) {
            assertThat(
                "Inventory value for key '" + entry.getKey() + "' should be an integer",
                entry.getValue(),
                instanceOf(Integer.class)
            );
        }

        // Validate keys reflect valid pet statuses ("available", "pending", "sold")
        Set<String> allowedKeys = Set.of("available", "pending", "sold");
        // Accept possibly more statuses, but verify expected keys exist
        assertThat("Inventory should contain 'available'", inventory, hasKey("available"));
        assertThat("Inventory should contain 'pending'", inventory, hasKey("pending"));
        assertThat("Inventory should contain 'sold'", inventory, hasKey("sold"));

        // Optional: check expected response values if needed (example expected)
        assertThat("Expected available", inventory.get("available"), equalTo(10));
        assertThat("Expected sold", inventory.get("sold"), equalTo(5));
        assertThat("Expected pending", inventory.get("pending"), equalTo(2));
    }}

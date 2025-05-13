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
 * Update Pet with Invalid ID
 * Attempt to update a pet using an invalid (string) ID value.
 * Path: /pet
 * Method: PUT
 */
public class PetUpdateNoIdTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Update Pet with Invalid ID - should return 400 Bad Request with error message")
    void updatePetWithInvalidId() {
        String requestBody = """
        {
          "id": "abc",
          "name": "Mittens",
          "category": {
            "id": 2,
            "name": "Cats"
          },
          "photoUrls": [
            "http://example.com/photos/mittens.jpg"
          ],
          "tags": [],
          "status": "sold"
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .put("/pet")
        .then()
            .statusCode(400)
            .contentType(ContentType.JSON)
            .body("code", equalTo(400))
            .body("message", equalTo("Invalid ID supplied"));
    }}

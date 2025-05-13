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
 * Update Non-existent Pet
 * Attempt to update a pet with a valid ID that does not exist in the store.
 * Path: /pet
 * Method: PUT
 */
public class PetNonExistUpdateTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    void updateNonExistentPet_ShouldReturn404NotFound() {
        // Prepare the request payload for a non-existent pet
        String requestBody = """
            {
                "id": 999999,
                "name": "Ghost",
                "category": {
                    "id": 3,
                    "name": "Exotic"
                },
                "photoUrls": [],
                "tags": [],
                "status": "pending"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .put("/pet")
        .then()
            .assertThat()
            .statusCode(404)
            .contentType(ContentType.JSON)
            .body("code", equalTo(404))
            .body("message", equalTo("Pet not found"));
    }}

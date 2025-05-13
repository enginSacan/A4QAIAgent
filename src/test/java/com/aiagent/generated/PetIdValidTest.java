package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Delete Pet with Valid petId and API Key
 * Verify that a pet is successfully deleted when a valid petId is provided along with a valid api_key header.
 * Path: /pet/{petId}
 * Method: DELETE
 */
public class PetIdValidTest {
    private static final String API_KEY = "validApiKey123";
    private static final long PET_ID = 1001L;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        createPetIfNotExists(PET_ID);
    }

    /**
     * Utility method to create a pet if it doesn't exist.
     * This makes the delete test idempotent.
     */
    private static void createPetIfNotExists(long petId) {
        Response getResponse = given()
                .pathParam("petId", petId)
                .get("/pet/{petId}");
        if (getResponse.statusCode() == 404) {
            // Create a simple pet object
            String petJson = "{ \"id\": " + petId + ", \"name\": \"TestPet\", \"photoUrls\": [\"http://example.com/photo\"] }";
            given()
                    .contentType("application/json")
                    .body(petJson)
                    .post("/pet")
                    .then()
                    .statusCode(anyOf(is(200), is(201)));
        }
    }

    @Test
    @Order(1)
    @DisplayName("Delete Pet with Valid petId and API Key")
    void deletePet_withValidPetIdAndApiKey_shouldSucceed() {
        // Step 1: Delete the pet
        Response deleteResponse = given()
                .header("api_key", API_KEY)
                .pathParam("petId", PET_ID)
        .when()
                .delete("/pet/{petId}")
        .then()
                .statusCode(200)
                // Response can sometimes contain a message or be empty:
                .body(anyOf(is(emptyOrNullString()), containsStringIgnoringCase("deleted"), containsString(Long.toString(PET_ID))))
                .extract().response();

        // Step 2: Verify response body is empty or confirms deletion
        String responseBody = deleteResponse.getBody().asString();
        Assertions.assertTrue(
                responseBody == null ||
                        responseBody.trim().isEmpty() ||
                        responseBody.toLowerCase().contains("deleted") ||
                        responseBody.contains(Long.toString(PET_ID)),
                "Response body should be empty or indicate successful deletion."
        );

        // Step 3: Verify pet can no longer be retrieved
        given()
                .pathParam("petId", PET_ID)
        .when()
                .get("/pet/{petId}")
        .then()
                .statusCode(404); // Pet not found
    }
}

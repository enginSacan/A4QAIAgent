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
 * Delete Non-existent Pet
 * Verify the API returns a 404 error when attempting to delete a pet that does not exist.
 * Path: /pet/{petId}
 * Method: DELETE
 */
public class PetIdErrorTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Delete Non-existent Pet should return 404 with informative error message")
    void deleteNonExistentPetReturns404() {
        int nonExistentPetId = 999999;

        Response response =
            RestAssured
                .given()
                    .header("api_key", "validApiKey123")
                    .pathParam("petId", nonExistentPetId)
                .when()
                    .delete("/pet/{petId}")
                .then()
                    .extract().response();

        // Assert that the status code is 404
        assertEquals(404, response.statusCode(),
                "Expected status code 404 for non-existent pet deletion");

        // Optionally, log response for visibility in case of test failure
        // System.out.println(response.asString());

        // Assert that the response contains a relevant error message
        String responseBody = response.asString();
        assertTrue(
                responseBody.toLowerCase().contains("not found")
                || responseBody.toLowerCase().contains("pet not found")
                || responseBody.toLowerCase().contains("does not exist"),
                "Expected error message for missing pet in response body"
        );
    }}

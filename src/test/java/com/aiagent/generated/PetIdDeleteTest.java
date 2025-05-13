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
 * Delete Pet with Invalid petId (Non-integer)
 * Verify the API returns a 400 error when petId is a non-integer value.
 * Path: /pet/{petId}
 * Method: DELETE
 */
public class PetIdDeleteTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Delete Pet with Invalid petId (Non-integer)")
    void deletePetWithNonIntegerPetIdShouldReturn400() {
        String invalidPetId = "abc";
        String apiKey = "validApiKey123";

        ValidatableResponse response =
            RestAssured
                .given()
                    .header("api_key", apiKey)
                .when()
                    .delete("/pet/{petId}", invalidPetId)
                .then()
                    .assertThat()
                    .statusCode(400)
                    // Optional: Check Content-Type header, if service returns one
                    .contentType(anyOf(equalTo(ContentType.JSON.toString()), containsString("json"), any(String.class)))
                    // Body should include error message about invalid ID
                    .body(not(emptyOrNullString()))
                    .body(
                        anyOf(
                            containsStringIgnoringCase("invalid"), // typical error keyword
                            containsStringIgnoringCase("id"),
                            containsStringIgnoringCase("supplied")
                        )
                    );
    }}

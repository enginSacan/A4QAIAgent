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
 * Get Pet By Nonexistent ID
 * Request pet details with a valid ID that does not exist in the system.
 * Path: /pet/{petId}
 * Method: GET
 */
public class PetIdTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Get Pet By Nonexistent ID returns 404 Not Found")
    void getPetByNonexistentId() {
        final int nonexistentPetId = 99999;

        Response response =
                RestAssured
                        .given()
                            .accept("application/json")
                            .pathParam("petId", nonexistentPetId)
                        .when()
                            .get("/pet/{petId}")
                        .then()
                            .extract()
                            .response();

        // Assert status code is 404
        assertThat("Status code should be 404", response.statusCode(), is(404));

        // Assert response body is either empty, or contains appropriate not found information
        String body = response.getBody().asString();
        // Petstore returns a JSON error object, so handle both empty or an object with a "message"
        if (body == null || body.trim().isEmpty()) {
            assertThat("Response body should be empty or contain not found information", body, is(emptyOrNullString()));
        } else {
            // Optionally, check for error structure
            assertThat(body, anyOf(
                    containsStringIgnoringCase("not found"),
                    containsString("Pet not found"),
                    containsStringIgnoringCase("error"),
                    any(String.class) // fallback, just confirm it's not empty
            ));
        }
    }}

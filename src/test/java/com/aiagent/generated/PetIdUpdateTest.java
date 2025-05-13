package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Update pet with valid data
 * Update an existing pet with a valid integer petId and valid name and status in the form data.
 * Path: /pet/{petId}
 * Method: POST
 */
public class PetIdUpdateTest {

    private static final Long PET_ID = 1010L;
    private static final String NEW_NAME = "testPet";
    private static final Object NEW_STATUS = "sold";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Update pet with valid data")
    void updatePetWithValidData() {
        // Update the pet via form data
        given()
            .contentType(ContentType.URLENC)
            .pathParam("petId", PET_ID)
            .formParam("name", NEW_NAME)
            .formParam("status", NEW_STATUS)
        .when()
            .post("/pet/{petId}")
        .then()
            .statusCode(200);

        // Validate the pet is updated by retrieving it and verifying the fields
        ValidatableResponse getResponse = given()
            .pathParam("petId", PET_ID)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(200)
            .body("id", equalTo(PET_ID))
            .body("name", equalTo(NEW_NAME))
            .body("status", equalTo(NEW_STATUS));
    }}

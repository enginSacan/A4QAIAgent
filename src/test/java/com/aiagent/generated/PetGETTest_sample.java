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
 * Find Pet by ID Test
 * Retrieves a pet from the store by its ID
 * Path: /pet/{petId}
 * Method: GET
 */
public class PetGETTest_sample {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    @DisplayName("Successful retrieval of existing pet")
    public void testGetExistingPet() {
        // This test assumes there's a pet with ID 1 in the store
        given()
            .pathParam("petId", 1)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1))
            .body("name", not(emptyOrNullString()))
            .body("status", anyOf(
                equalTo("available"), 
                equalTo("pending"), 
                equalTo("sold")
            ));
    }
    
    @Test
    @DisplayName("Pet not found with non-existent ID")
    public void testGetNonExistentPet() {
        // Using a very large ID that is unlikely to exist
        given()
            .pathParam("petId", 9999999)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(404);
    }
    
    @Test
    @DisplayName("Invalid ID format returns 400")
    public void testGetPetWithInvalidId() {
        given()
            .pathParam("petId", "invalid")
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(400);
    }
}
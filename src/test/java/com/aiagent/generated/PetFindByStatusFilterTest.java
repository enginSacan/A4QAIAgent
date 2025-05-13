package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Find pets by single valid status
 * Verify API returns pets when filtering by a single valid status 'available'.
 * Path: /pet/findByStatus
 * Method: GET
 */
public class PetFindByStatusFilterTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Find pets by single valid status - available")
    void shouldReturnPetsWithAvailableStatus() {
        Response response = RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .queryParam("status", "available")
                .when()
                    .get("/pet/findByStatus")
                .then()
                    .assertThat()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract().response();

        // Assert response is an array
        List<Map<String, Object>> pets = response.jsonPath().getList("$");
        assertThat("Response body should be a JSON array", pets, is(notNullValue()));

        // Assert every pet has status "available"
        for (Map<String, Object> pet : pets) {
            // Defensive: Only check if status key exists, which should be the case by the API contract
            assertThat("Each pet should have a status property", pet, hasKey("status"));
            assertThat("Each pet's status should be 'available'", pet.get("status"), is("available"));
        }
    }}

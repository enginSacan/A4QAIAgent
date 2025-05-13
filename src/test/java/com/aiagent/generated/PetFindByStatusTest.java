package com.aiagent.generated;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Find pets by multiple valid statuses
 * Verify API returns pets for multiple comma-separated status values: 'available,pending'.
 * Path: /pet/findByStatus
 * Method: GET
 */
public class PetFindByStatusTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Find pets by multiple valid statuses: available,pending")
    void findPetsByMultipleValidStatuses() {
        Response response = RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .queryParam("status", "available,pending")
                .when()
                    .get("/pet/findByStatus")
                .then()
                    .assertThat()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                    .extract()
                        .response();

        // Assert the response is a JSON array
        assertThat("Response is not a JSON array", response.jsonPath().getList("$"), is(notNullValue()));
        List<?> pets = response.jsonPath().getList("$");
        assertThat("Response is not an array", pets, is(instanceOf(List.class)));
        
        // If there are pets returned, validate all of their statuses
        List<String> statuses = response.jsonPath().getList("status");
        assertThat("Not all pets have status 'available' or 'pending'",
                   statuses,
                   everyItem(anyOf(equalTo("available"), equalTo("pending"))));
    }}

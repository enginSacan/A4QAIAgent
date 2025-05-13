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

/**
 * Get Pet By Valid ID
 * Request pet details using a valid existing petId.
 * Path: /pet/{petId}
 * Method: GET
 */
public class PetIdExistTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Get Pet By Valid ID")
    void getPetByValidId() {
        int petId = 12345;

        Response response =
            given()
                .header("Accept", "application/json")
                .pathParam("petId", petId)
            .when()
                .get("/pet/{petId}")
            .then()
                .assertThat()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(12345))
                    .body("name", equalTo("Doggie"))
                    .body("category.id", equalTo(1))
                    .body("category.name", equalTo("Dogs"))
                    .body("photoUrls", hasSize(greaterThanOrEqualTo(1)))
                    .body("photoUrls", hasItem("http://example.com/photo1.jpg"))
                    .body("tags", hasSize(greaterThanOrEqualTo(1)))
                    .body("tags[0].id", equalTo(1))
                    .body("tags[0].name", equalTo("tag1"))
                    .body("status", equalTo("available"))
                .extract().response();

        // Additional assertions to check all expected fields are present and valid
        // Check the presence and types of each field

        // id
        assertThat(response.path("id"), is(instanceOf(Number.class)));
        // name
        assertThat(response.path("name"), is(instanceOf(String.class)));

        // category
        assertThat(response.path("category"), is(notNullValue()));
        assertThat(response.path("category.id"), is(instanceOf(Number.class)));
        assertThat(response.path("category.name"), is(instanceOf(String.class)));

        // photoUrls
        assertThat(response.path("photoUrls"), is(notNullValue()));

        // tags
        assertThat(response.path("tags"), is(notNullValue()));

        // status
        assertThat(response.path("status"), is(instanceOf(String.class)));
    }}

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
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Update Pet with Valid Data
 * Update an existing pet with a valid JSON payload and expect a successful update.
 * Path: /pet
 * Method: PUT
 */
public class PetUpdateTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

@Test
    @DisplayName("Update Pet with Valid Data")
    void updatePetWithValidData() {
        // Prepare request body as a Java Map
        Map<String, Object> requestBody = Map.of(
                "id", 123,
                "name", "Fido",
                "category", Map.of(
                        "id", 1,
                        "name", "Dogs"
                ),
                "photoUrls", List.of("http://example.com/photos/fido.jpg"),
                "tags", List.of(
                        Map.of(
                                "id", 1,
                                "name", "friendly"
                        )
                ),
                "status", "available"
        );

        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .put("/pet")
                .then()
                    .statusCode(200)
                    // Validate top-level fields
                    .body("id", equalTo(123))
                    .body("name", equalTo("Fido"))
                    .body("status", equalTo("available"))
                    // Validate nested 'category' object
                    .body("category.id", equalTo(1))
                    .body("category.name", equalTo("Dogs"))
                    // Validate 'photoUrls' array
                    .body("photoUrls", hasSize(1))
                    .body("photoUrls[0]", equalTo("http://example.com/photos/fido.jpg"))
                    // Validate 'tags' array and contents
                    .body("tags", hasSize(1))
                    .body("tags[0].id", equalTo(1))
                    .body("tags[0].name", equalTo("friendly"))
                    .extract().response();

        // Extra assertion to ensure the response body matches the request body (optional, for completeness)
        // You can use JSONAssert or perform deep equals if your project requires strict validation
    }}

package tests.basics;

import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JSONSchemaValidation_8 {

    /*
    * Response validation - The validation depends on data.
    * Schema validation - The validation depends on type of data like int, String.
    */

    @Test
    public void verifyJsonSchema() {
        given()
                .when()
                .get("http://localhost:9000/employees")
                .then()
                .assertThat()
                /*      Validating the json schema by placing a file in project     */
                .body(JsonSchemaValidator.matchesJsonSchema(new File(System.getProperty("user.dir")+"/src/test/java/jsonFiles/sutdentJsonSchema.json")));
//                .body(RestAssuredMatchers.matche)
    }
}

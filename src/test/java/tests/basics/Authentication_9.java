package tests.basics;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Authentication_9 {
    /*
    * Authentication - Whether response is valid or not.
    * Authorization - Whether user has access or not.
    *
    * All Authentication should be part of given()
    * */

    /*
    * Types of authentication:-
    * Basic - By providing username and password basic authentication is done.
    * Digest
    * Preemptive
    * Bearer Token
    * Oauth2.0
    * API Key
    */

    @Test
    public void basicAuthenticationWithPostmanAPI() {
        given()
                .auth()
                .basic("postman","password")
                .when()
                .get("https://postman-echo.com/basic-auth")
                .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .log().all();
    }

    @Test
    public void digestAuthenticationWithPostmanAPI() {
        given()
                .auth()
                .digest("postman","password")
                .when()
                .get("https://postman-echo.com/basic-auth")
                .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .log().all();
    }

    @Test
    public void preemptiveAuthenticationWithPostmanAPI() {
        given()
                .auth()
                .preemptive()
                .basic("postman","password")
                .when()
                .get("https://postman-echo.com/basic-auth")
                .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .log().all();
    }

    @Test
    public void bearerTokenAuthentication() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "manojhonda691@gmail.com");
        jsonObject.put("password", "manu7829");
        System.out.println(jsonObject.toString());

        Response response = given()
                .contentType("application/json")
                .body(jsonObject.toString())
                .when()
                .post("http://localhost:9000/auth/login");

        /*  Validating the status code  */
        response.then().assertThat().statusCode(200);

        /*      Getting an access token     */
        String access_Token = response.jsonPath().getJsonObject("access_token");
        System.out.println(access_Token);

        /*  The access token need to be passed as bearer token  */
        given()
                .headers("Authorization", "Bearer "+access_Token)
                .when()
                .get("http://localhost:9000/users/1")
                .then()
                .statusCode(200)
                .log().body();
    }

    /*  API Key authentication from weather API */
}

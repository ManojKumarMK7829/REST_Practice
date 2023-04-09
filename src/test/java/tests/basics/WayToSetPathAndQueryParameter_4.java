package tests.basics;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class WayToSetPathAndQueryParameter_4 {

    @Test
    public void setPathAndQueryParameter() {
        given()
                /*  Setting path parameter */
                .pathParams("numberOfUser", "users")
                /*  Setting up query parameter  */
                .queryParam("id",4)
                .queryParam("userId", 4)
                /*
                 *   Usually the whole URI will along with one path param
                 * http://localhost:9000/students?id=1&age=18 -> Complete URI, along with domain-> http://localhost:9000
                 * ?id=1&age=18 <- these are the query parameter mentioned
                 * As path parameter and query parameter was set under given(), no need specify in the URI
                 *
                 * */
                .when()
                /*  In the URL we specify path parameter in flower braces "{}" -> {numberOfUser}    */
                /*  No need to specify query params if its declared under given()   */
                .get("http://localhost:9000/{numberOfUser}")
                .then()
                .assertThat().statusCode(200)
                .log().all();
    }
}

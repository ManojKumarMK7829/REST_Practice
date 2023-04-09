package tests.basics;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class ValidateHeaders_6 {

    @Test
    public void testHeaderValues() {
        Response resp = given()
                .when()
                .get("https://www.google.com/");
        resp.then().assertThat().statusCode(200);

        Headers headers = resp.getHeaders();
        Assert.assertTrue(headers.hasHeaderWithName("Content-Encoding"), "The specified header is missing");
        headers.forEach(header -> {
            if(header.getName().equals("Server")) {
                System.out.println(header.getValue());
                Assert.assertTrue(header.getValue().equals("gws"), "The specified value is absent");
                return;
            }
        });
        /*  Donot use log().all() it will print all */
    }
}

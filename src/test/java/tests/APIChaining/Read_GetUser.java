package tests.APIChaining;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Read_GetUser {

    @Test(groups = {"user"})
    public void getUser(ITestContext context) {
        String access_Token = (String) context.getAttribute("access_token");
        int id = (int) context.getAttribute("id");
        given()
                .headers("Authorization" , "Bearer "+access_Token)
                .when()
                .get("http://localhost:9000/students/"+id)
                .then()
                .assertThat().statusCode(200)
                .log().all();
    }
}

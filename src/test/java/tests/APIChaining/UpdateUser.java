package tests.APIChaining;

import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUser {

    @Test(groups = {"user"})
    public void updateUserRecord(ITestContext context) {
        /*  Got access token and id to modify resource  */
        String access_Token = (String) context.getAttribute("access_token");
        int id = (int) context.getAttribute("id");

        JSONObject jsonObjectMobile = new JSONObject();
        jsonObjectMobile.put("Mobile", "147258369");
        JSONObject jsonObjectHome = new JSONObject();
        jsonObjectHome.put("Home", "741852963");
        JSONObject jsonObjectArray [] = {jsonObjectMobile, jsonObjectHome};
        JSONArray jsonArray = new JSONArray(jsonObjectArray);
        JSONObject jsonData = new JSONObject();
        jsonData.put("firstName", "Kawasaki");
        jsonData.put("lastName", "Ninja ZX10R");
        jsonData.put("age", "26");
        jsonData.put("state", "KA");
        jsonData.put("phoneNumbers", jsonArray);
        System.out.println(jsonData.toString());

        given()
                .headers("Authorization" , "Bearer "+access_Token)
                .contentType(ContentType.JSON)
                .body(jsonData.toString())

                .when()
                .put("http://localhost:9000/students/"+id)

                .then()
                .statusCode(200)
                .body("lastName", equalTo("Ninja ZX10R"))
                .log().all();
    }
}

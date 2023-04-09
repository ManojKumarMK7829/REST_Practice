package tests.APIChaining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import tests.pojoExample.StudentsData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUser {

    @Test(groups = {"user","regression"})
    public void createUser(ITestContext context) throws JsonProcessingException {
        /*  Getting the access  */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "manojhonda691@gmail.com");
        jsonObject.put("password", "manu7829");
        System.out.println(jsonObject.toString());

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonObject.toString())
                .when()
                .post("http://localhost:9000/auth/login");

        /*  Validating the status code  */
        response.then().assertThat().statusCode(200);

        /*      Getting an access token     */
        String access_Token = response.jsonPath().getJsonObject("access_token");

        /*  Setting a value for context     */
        context.setAttribute("access_token", access_Token);
        System.out.println(access_Token);

        /*  After getting the access, use access token to create data   */
        /*  By POJO create a pay load   */
        StudentsData.PhoneNumbers phoneNumbersMobile = new StudentsData.PhoneNumbers();
        phoneNumbersMobile.setMobileNumber("321654987");
        StudentsData.PhoneNumbers phoneNumbersHome = new StudentsData.PhoneNumbers();
        phoneNumbersHome.setMobileNumber("987654321");
        StudentsData.PhoneNumbers phoneNumbers[] = {phoneNumbersMobile, phoneNumbersHome};
        StudentsData studentsData = new StudentsData();
        studentsData.setFirstName("Kawasaki");
        studentsData.setLastName("Ninja ZXSR");
        studentsData.setAge("26");
        studentsData.setState("KA");
        studentsData.setPhoneNumbers(phoneNumbers);

        String jsonData = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(studentsData.toString());

        Response resp = given()
                .headers("Authorization", "Bearer "+access_Token)
                .body(jsonData)
                .contentType(ContentType.JSON)
                .when()
                .post("http://localhost:9000/students");
        resp.then()
                .statusCode(201)
                .body("firstName", equalTo("Kawasaki"))
                .log().all();

        /*  Setting id for context  */
        context.setAttribute("id", resp.body().jsonPath().getJsonObject("id"));
    }
}

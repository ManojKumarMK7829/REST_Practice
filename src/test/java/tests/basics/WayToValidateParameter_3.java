package tests.basics;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class WayToValidateParameter_3 {

    @Test
    public void validateCorrectParameterPresentForAKey() {
        /*  In order to check the response of specific parameter we should get response first   */
        /*  Getting a response first, we should end at get()    */
        Response resp = given()
                .when()
                .get("http://localhost:9000/users");
        /*  Validating the status code as 200   */
        resp.then()
                .assertThat().statusCode(200);
        Assert.assertEquals(200, resp.getStatusCode(), "Status code did not match");
        /*  Validating the parameter    */
        List<String> jsonObject = resp.jsonPath().getJsonObject("phoneNumber");
        for(String key : jsonObject){
            if(key.equals("11001100110")) {
                System.out.println(key+" value is validated");
                Assert.assertTrue(key.equals("11001100110"));
            }
        }
    }

    @Test
    public void validateParameterByNavigatingToItsParameter() {
        given()
                .when()
                .get("http://localhost:9000/students")

                .then()
                /*      By providing the path of required parameter validations are carried     */
                /*      Multiple bodies can be placed to verify different parameter     */
                /*      Its not that much supported becoz if requirement order changes then hard coded
                value of key will be failed.    */
                .body("[5].lastName", equalTo("Hornet300"))
                .log().body();
    }

    @Test
    public void validateCorrectParameterPresentForAKeyUsingJSONObject() throws ParseException {
        /*  In order to check the response of specific parameter we should get response first   */
        /*  Getting a response first, we should end at get()    */
        Response resp = given()
                .when()
                .get("http://localhost:9000/employees");
        /*  Validating the status code as 200   */
        resp.then()
                .assertThat().statusCode(200);
        Assert.assertEquals(200, resp.getStatusCode(), "Status code did not match");
        /*  Validating the parameter    */
        /*  In order to check only within an array of whole JSON    */

        JSONObject jsonObject = new JSONObject(resp.asString());
        for(int i=0 ; i< jsonObject.getJSONArray("employeeDetails").length(); i++) {
            String phoneNumber = jsonObject.getJSONArray("employeeDetails").getJSONObject(i).get("mail").toString();
            System.out.println(phoneNumber);
        }
        /*
        * Can be validated like below example too
        *
        List<String> jsonObject = resp.jsonPath().getJsonObject("employeeDetails.mail");//Specify the path
        for(String key : jsonObject){
                System.out.println(key+" value is validated");
        }
         // Instead for each loop we can write like this too
        jsonObject.stream().forEach(value -> System.out.println(value));
         */
    }
}

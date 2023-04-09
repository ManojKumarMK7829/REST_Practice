package tests.basics;

import org.testng.annotations.Test;
import java.util.HashMap;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PracticeBasic_1 {

    int id;

    @Test(priority = 1, groups = {"parallelTest"})
    public void getRequest() {
        /*  In given section we are writing request specification   */
        given()
                /*  In when() section we are specifying HTTP method ie, what type of request either GET,PUT,POST,DELETE     */
                .when().get("http://localhost:9000/users")
                /*  In then section validate the response received by extracting headers, cookies and response body     */
                .then()
                /*  Validating the status code  */
                .statusCode(200)
                /*  In order to print in console    */
                .log().all()
        ;
    }

    @Test(groups = {"parallelTest"})
    public void getResponseBody(){
        /*  Example of HTML as response */
        given()
                .when()
                .get("http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1")
                .then()
                .log().all();

    }

    @Test(groups = {"parallelTest"})
    public void validateTheExactResponse() {
        /*  When there is no request specification there is no need to use given(), we can directly use when()  */
        when()
                /*  Need to be specific URI which gives exact response for the URL  */
                .get("http://localhost:9000/users/55")
                .then()
                /*  Validating its status code  */
                .statusCode(200)
                /*  Validating its body that firstName and phoneNumber  */
                .body("firstName", equalTo("Sam"))
                .body("phoneNumber", equalTo("89898989898"))
                /*  In order to print it in console */
                .log().all();
    }

    @Test
    public void createANewData() {
        /*  POST request creates a data in the server   */
        /*  In order to create data we need to have a json data or payload  */
        HashMap hashMap = new HashMap<>();
        hashMap.put("firstName","Khabib");
        hashMap.put("lastName","Ahmdulila");
        hashMap.put("phoneNumber","10101010101");
        hashMap.put("emailAddress","khabibAhmdulila@UFC.com");
        /*  Its not a good practice to use hash map becoz all values will be hard coded */
        /*  This data is passed to body("") under given() data  */
        given()
                .contentType("application/json")
                .body(hashMap)

                .when()
                .post("http://localhost:9000/users")

                .then()
                /*  Validating the post request status  */
                .statusCode(201)
                .log().all();
    }

    @Test
    public void alterTheDataByCreatingNewData() {
        /*  POST request creates a data in the server   */
        /*  Its not a good practice to use hash map becoz all values will be hard coded */
        /*  In order to create data we need to have a json data or payload  */
        HashMap hashMap = new HashMap<>();
        hashMap.put("firstName","Under");
        hashMap.put("lastName","Taker");
        hashMap.put("phoneNumber","10101022101");
        hashMap.put("emailAddress","underTaker@wwe.com");
        /*  Its not a good practice to use hash map becoz all values will be hard coded */
        /*  This data is passed to body("") under given data  */
        int id = given()
                .contentType("application/json")
                .body(hashMap)

                .when()
                .post("http://localhost:9000/users")
                .jsonPath()
                /*  Getting the newly created user id need to specify the jsonPath()  */
                .getInt("id");
                /*  The lines of code is returning its id to alter its data */
                /*  Clearing the hashmap and altering its data created by above hashmap are corrected    */
                hashMap.clear();
        hashMap.put("userId",id);
        hashMap.put("firstName","Under");
        hashMap.put("lastName","Taker");
        hashMap.put("phoneNumber","10101022101");
        hashMap.put("emailAddress","underTaker@WWE.com");
        /*  Below code is written to alter its data */
        given()
                .contentType("application/json")
                .body(hashMap)
        /*  In order to correct put() method is used under when()    */
                .when()
                .put("http://localhost:9000/users/"+id)
                .then()
                /*  Validating the post request status  */
                .statusCode(200)
                .body("emailAddress", equalTo("underTaker@WWE.com"))
                .log().all();
    }

    /*
    * Introducing "dependsOnMethods" on tag in testng where if A() method depends on B()
    *  the priority given to B() first
    * and depends tag is introduced in A().
    * */

    @Test(priority = 2)
    public void creatingANewDataAndReturningItsID() {
        HashMap hashMap = new HashMap<>();
        hashMap.put("firstName","Brock");
        hashMap.put("lastName","Lesnar");
        hashMap.put("phoneNumber","10101033101");
        hashMap.put("emailAddress","brockLesnar@wwe.com");
        /*  Its not a good practice to use hash map becoz all values will be hard coded */
        id =  given()
                .contentType("application/json")
                .body(hashMap)

                .when()
                .post("http://localhost:9000/users")
                .jsonPath()
                /*  Getting the newly created user id need to specify the jsonPath()  */
                .getInt("id");
    }

    @Test(priority = 3, dependsOnMethods = "creatingANewDataAndReturningItsID")
    public void modifyingTheDataCreatedByDependedMethod() {
        HashMap hashMap = new HashMap<>();
        hashMap.put("userId", id);
        hashMap.put("firstName","Brock");
        hashMap.put("lastName","Lesnar");
        hashMap.put("phoneNumber","10101033101");
        hashMap.put("emailAddress","brockLesnar@wwe.com");

        given()
                .contentType("application/json")
                .body(hashMap)
                .when()
                .put("http://localhost:9000/users/"+id)
                .then()
                /*  Validating the userId is same as id */
                .body("userId", equalTo(id))
                .log().all();
    }

    @Test(priority = 4)
    public void deleteUserCreated() {
        given()
                .when()
                /*  Delete the created user */
                .delete("http://localhost:9000/users/"+id)
                .then()
                .statusCode(200)
                .log().all();
    }
    /*
    * log().all() -> Print all the things like cookies, body and headers
    * log().body() -> print only the body
    * log().cookies() -> print only the cookies
    * log().headers() -> print only the headers
    * */
}
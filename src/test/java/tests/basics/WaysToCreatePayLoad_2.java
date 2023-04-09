package tests.basics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import tests.pojoExample.StudentsData;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class WaysToCreatePayLoad_2 {
    /*
    * Ways to create pay load or request body
    * 1) Hashmap    -> Not recommended becoz of hard coded values in order refer follow practiceBasic class
    * 2) Using org.json
    * 3) Using POJO(Plain Old Java Object)
    * 4) Using an external JSON file
    *
    * */

    int id;

    @Test
    public void usingHashMapToCreateInput() {
        /*  To create a body for post request   */
        HashMap hashMap = new HashMap<>();
        hashMap.put("firstName", "Yamaha");
        hashMap.put("lastName", "MT15");
        hashMap.put("age", "19");
        hashMap.put("state", "KA");
        HashMap mapOfMobileNumber = new HashMap<>();
        mapOfMobileNumber.put("Mobile","111-111-5111");
        HashMap mapOfHome = new HashMap<>();
        mapOfHome.put("Home","222-222-5222");
        JSONObject jsonObjectMobile = new JSONObject(mapOfMobileNumber);
        JSONObject jsonObjectHome = new JSONObject(mapOfHome);
        JSONObject contactNumbers[] = {jsonObjectMobile, jsonObjectHome};
        JSONArray contactNumber = new JSONArray(contactNumbers);
        /*
        Another way to place JSON object to array
        JSONArray contactNumber = new JSONArray();
        contactNumber.put(jsonObjectMobile);
        contactNumber.put(jsonObjectHome);
         */
        hashMap.put("phoneNumbers", contactNumber);
        JSONObject jsonObjectOfWholeJson = new JSONObject(hashMap);
        System.out.println(jsonObjectOfWholeJson.toString());

        given()
                .contentType("application/json")
                /*  Passing same json object to body()  */
                .body(jsonObjectOfWholeJson.toString())

                .when()
                .post("http://localhost:9000/students")

                .then()
                .statusCode(201)
                .body("firstName", equalTo("Yamaha"))
                /*  Verifying the content type of the header    */
                .header("Content-Type", "application/json; charset=utf-8")
                .log().all();
    }

    @Test
    public void usingOnlyJSONObjectToCreatePayLoad() {
        /*  Without using hash map  */

        JSONObject jsonObjectMobile = new JSONObject();
        jsonObjectMobile.put("Mobile","111-111-5111");
        JSONObject jsonObjectHome = new JSONObject();
        jsonObjectHome.put("Home","222-222-5222");
        JSONObject contactNumbers[] = {jsonObjectMobile, jsonObjectHome};
        JSONArray contactNumber = new JSONArray(contactNumbers);
        JSONObject jsonObjectOfWholeJson = new JSONObject();
        jsonObjectOfWholeJson.put("firstName", "Yamaha");
        jsonObjectOfWholeJson.put("lastName", "R15");
        jsonObjectOfWholeJson.put("age", "19");
        jsonObjectOfWholeJson.put("state", "KA");
        jsonObjectOfWholeJson.put("phoneNumbers", contactNumber);
        System.out.println(jsonObjectOfWholeJson.toString());

        given()
                .contentType("application/json")
                /*  Passing same json object to body()  */
                .body(jsonObjectOfWholeJson.toString())

                .when()
                .post("http://localhost:9000/students")

                .then()
                .statusCode(201)
                .body("lastName", equalTo("R15"))
                /*  Verifying the content type of the header    */
                .header("Content-Type", "application/json; charset=utf-8")
                .log().all();
    }

    @Test
    public void usingOnlyPOJOClasses() throws JsonProcessingException {
        /*  Using only POJO Classes without hashmap or org.Json library */
        /*  Add Lombok dependency to get setters and getters for optimized  */

        StudentsData studentsData = new StudentsData();
        StudentsData.PhoneNumbers phoneNumbersOfMobile = new StudentsData.PhoneNumbers();
        phoneNumbersOfMobile.setMobileNumber("123456789");
        StudentsData.PhoneNumbers phoneNumbersOfHome = new StudentsData.PhoneNumbers();
        phoneNumbersOfHome.setHomeNumber("987654321");
        StudentsData.PhoneNumbers phoneNumbers[] = {phoneNumbersOfMobile, phoneNumbersOfHome};
        studentsData.setPhoneNumbers(phoneNumbers);
        studentsData.setFirstName("Honda");
        studentsData.setLastName("CB300");
        studentsData.setAge("20");
        studentsData.setState("KA");

        String jsonObjectOfWholeJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(studentsData);
        System.out.println(jsonObjectOfWholeJson);

        given()
                .contentType("application/json")
                /*  Passing same json object to body()  */
                .body(jsonObjectOfWholeJson.toString())

                .when()
                .post("http://localhost:9000/students")

                .then()
                .statusCode(201)
                .body("lastName", equalTo("CB300"))
                /*  Verifying the content type of the header    */
                .header("Content-Type", "application/json; charset=utf-8")
                .log().all();
    }

    @Test
    public void usingJSONFileAsAPayload() throws Exception {
        /*  Specify the file path of the JSON file   */
        FileReader fileReader = new FileReader(new File("C:\\Users\\User\\IdeaProjects\\PracticeREST\\src\\test\\java\\jsonFiles\\studentData.json"));
        /*  The same path is passed to JSON parser and instance of JSON parser is passed to JSON object which creates JSON at runtime   */
        JSONObject jsonObjectOfWholeJson = new JSONObject((Map) new JSONParser().parse(fileReader));
        System.out.println(jsonObjectOfWholeJson.toString());

        given()
                .contentType(ContentType.JSON)
                /*  Passing same json object to body()  */
                .body(jsonObjectOfWholeJson.toString())

                .when()
                .post("http://localhost:9000/students")

                .then()
                .statusCode(201)
                .body("lastName", equalTo("Hornet300"))
                /*  Verifying the content type of the header    */
                .header("Content-Type", "application/json; charset=utf-8")
                .log().all()
                /*  By io.restAssure we are able to validate status code as 201 after log().all()   */
                .assertThat().statusCode(201)
        ;
    }
}
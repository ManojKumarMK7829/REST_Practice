package tests.basics;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ValidatePressenceCookies_5 {

    /*  Value of cookies always keep changing for every request sent
    *   So we cannot validate the exact "values" of cookies
    *   But we can validate the "Keys" of cookies
    * */

    @Test
    public void validateCookies() {
        Response resp = given()
                .when()
                .get("https://www.google.com/");
        resp.then().assertThat().statusCode(200);
        Map<String, String> respCookies = resp.getCookies();

        /*
         Ways to print keys
        respCookies.forEach((key,value)-> System.out.println(key +" : "+value));
        System.out.println("============================================================");
        respCookies.keySet().stream().forEach(key -> System.out.println(key));
        System.out.println("============================================================");
        respCookies.keySet().forEach(key -> System.out.println(key +" : "+ respCookies.get(key)));
        */

        /*  Validating specific key present in maps     */
        Assert.assertTrue(respCookies.containsKey("NID"),"Required cookie is absent");
        respCookies.forEach((key, value) -> {
            if(key.equals("NID"))
                System.out.println(key+" : "+ value);
        });
        /*  Donot place "log().all()" at the end of script becoz by default its going to print all cookies and values   */
    }
}

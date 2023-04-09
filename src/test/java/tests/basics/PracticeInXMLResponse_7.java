package tests.basics;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PracticeInXMLResponse_7 {
    /* http://restapi.adequateshop.com/swagger/ui/index#!/Traveler/Traveler_GetTraveler */
    /* For this refer above URL */

    @Test
    public void getAllTravellerInfo () {
        given()
                .when()
                /*  Passing the XML Url for the response    */
                .get("http://restapi.adequateshop.com/api/Traveler")
                .then()
                /*  Validating the status code  */
                .statusCode(200)
                /*  Validating Content-type of XML response     */
                .header("Content-Type", "application/xml; charset=utf-8")
                /*
                *   1. For XML to validate the parameter of the body we need to write xpath for it
                *   2. If there are multiple elements in xpath without indexing we cannot proceed
                *   3. Instead of '/' we need to specify '.' to traverse to child
                *   4. Indexing starts from '0' not from '1'
                * */
                .body("TravelerinformationResponse.travelers.Travelerinformation[0].id",equalTo("11133"))
                .log().body();
    }

    @Test
    public void validationThroughTestNgAssertion() {
        Response resp = given()
                .when()
                .get("http://restapi.adequateshop.com/api/Traveler");
        /*  Validating response status code  */
        Assert.assertEquals(resp.getStatusCode(), 200, "Status of response did not match");
        /*  Validating the parameter    */
        String parameter = resp.xmlPath().get("TravelerinformationResponse.travelers.Travelerinformation[0].name").toString();
        Assert.assertEquals(parameter, "Developer");

    }

    @Test
    public void validationFromXMLClass() {
        Response resp = given()
                .when()
                .get("http://restapi.adequateshop.com/api/Traveler");
        /*  By creating XMLPath object and passing response as an argument will get xml path    */
        /*  List of travellars will be drawn by getList() method passing xml path to it as an arguments */
        /*  If the response is in XML we need to use XMLPath and if its is in JSON then we need to use JSONPath*/
        XmlPath xmlPath = new XmlPath(resp.asString());
        List<String> listOfTraveller = xmlPath.getList("TravelerinformationResponse.travelers.Travelerinformation");
        Assert.assertEquals(listOfTraveller.size(), 10);

        /*  If the xml nodes are not constant in their placement then its better to get that list and validate it   */
        List<String> listOfTravellarsName = xmlPath.getList("TravelerinformationResponse.travelers.Travelerinformation.name");
        /*  Printing all the values of the list */
        listOfTravellarsName.stream().forEach(System.out::println);
        /*  In order to get validation  */
        AtomicBoolean status = new AtomicBoolean(Boolean.FALSE);
        listOfTravellarsName.stream().forEach(value -> {
            if(value.equals("Ashor")){
                status.set(Boolean.TRUE);
                return;
            }
        });
        Assert.assertTrue(status.get(), "Required name in the list is absent.");
    }
}

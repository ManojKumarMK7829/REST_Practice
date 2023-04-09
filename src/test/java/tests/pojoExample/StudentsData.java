package tests.pojoExample;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StudentsData {
    /* Use lombok dependency to get setters and getters */
    /*  Keep every variable name same as key present in JSON or whichever key has to be stored in JSON  */

    private String firstName;
    private String lastName;
    private String age;
    private String state;
    private PhoneNumbers phoneNumbers[];

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PhoneNumbers {
        @JsonProperty("Mobile")
        private String mobileNumber;
        @JsonProperty("Home")
        private String homeNumber;
    }
}
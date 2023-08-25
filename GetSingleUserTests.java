package ru.academits.reqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.when;
import static javax.swing.UIManager.get;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;

public class GetSingleUserTests {

    @Test
    public void getUser() {
        Response response = RestAssured
                .get("https://reqres.in/api/users/2");
        response.andReturn();
        response.prettyPrint();
    }

    @Test
    public void getUserWithParam() {
        ValidatableResponse response = (ValidatableResponse) RestAssured
                .given();
        ((RequestSpecification) response).queryParam("email", "janet.weaver@reqres.in");
                .when();
                .get("https://reqres.in/api/users/2");
        ((RequestSpecification) response).then();
        response.statusCode(200);

    }

    @Test
    public void getUserWithParamInMap() {
        Map<String, String> params = new HashMap<>();
        params.put("first_name", "Janet");
        params.put("last_name", "Weaver");

        Response response = (Response) RestAssured
                .given();
        ((RequestSpecification) response).queryParams(params);
                .get("https://reqres.in/api/users/2");
        response.andReturn();
        response.prettyPrint();
    }

    @Test
    public void getUserStatusCode() {
        Response response = RestAssured
                .get("https://reqres.in/api/users/2");
        response.andReturn();
        response.prettyPrint();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);

        assertEquals("Unexpected status code", 200, response.statusCode());
    }

    @Test
    public void testParseJson() {
        JsonPath response = (JsonPath) RestAssured
                .given();
                .get("https://reqres.in/api/users/2");
        response.jsonPath();
        response.prettyPrint();

        Assertions.assertEquals("janet.weaver@reqres.in", response.get("email").toString());
        Assertions.assertEquals("Janet", response.get("first_name").toString());
        Assertions.assertEquals("Weaver", response.get("last_name").toString());
        Assertions.assertEquals("https://reqres.in/img/faces/2-image.jpg", response.get("avatar").toString());
        Assertions.assertNotEquals(0, response.get("id").toString().length());
    }

    @Test
    public void testJsonKey() {
        Response response = RestAssured
                .get("https://reqres.in/api/users/2");
        response.andReturn();
        response.prettyPrint();

        String[] expectedKeys = {"id", "email", "first_name", "last_name", "avatar"};
        for (String expectedKey : expectedKeys) {
            response.then().assertThat().body("$", hasKey(expectedKey));
        }
    }
}

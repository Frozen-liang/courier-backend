package com.sms.satp.engine.auth;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.sms.satp.ApplicationTests;
import com.sms.satp.engine.auth.filter.OnePlatformAuthFilter;
import com.sms.satp.engine.auth.filter.ServiceMeshAuthFilter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author paderlol
 * @date: 2020/2/20 14:11
 */
@SpringBootTest(classes = ApplicationTests.class)
@Disabled
public class AuthFilterTest {

    @Autowired
    private OnePlatformAuthFilter onePlatformAuthFilter;

    @Autowired
    private ServiceMeshAuthFilter serviceMeshAuthFilter;

    @Test
    public void login_moon_test() {
        given().auth().none().filter(onePlatformAuthFilter).
            baseUri("https://onedev.smsassist.com").when().get("/Home").then().assertThat()
            .body(containsString("div class=\"db-1\" data-type=\"dashboard\""));


    }


    @Test
    public void jwt_token_test() {
        given().auth().none().filter(serviceMeshAuthFilter).
            baseUri("https://meshdev.smsassist.com").when().get("/eventlogger/v1/eventLog/")
            .then().assertThat().statusCode(404).body("message", equalTo("No message available"));


    }
}

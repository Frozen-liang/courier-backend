package com.sms.satp.engine;

import com.sms.satp.ApplicationTests;
import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.service.ApiExecuteService;
import com.sms.satp.parser.common.HttpMethod;
import com.sms.satp.parser.common.MediaType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiExecuteServiceImplTest {

    @LocalServerPort
    private int port;
    @Autowired
    private ApiExecuteService apiExecuteService;

    private String baseUrl;

    @BeforeEach
    public void before() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void uses_get_method() {
        Map<String, String> param = new HashMap<>();
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).
                path("/greeting").enableExecutionTime(true).httpMethod(HttpMethod.GET).
                queryParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_get_method_2() {
        Map<String, String> param = new HashMap<>();
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).
                path("/greeting?name={name}").pathParams(param).enableExecutionTime(true).httpMethod(HttpMethod.GET).
                queryParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_post_method() {
        Map<String, String> param = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).headers(headers).
                path("/greeting").enableExecutionTime(true).httpMethod(HttpMethod.POST).
                formParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_post_method_2() {
        Map<String, String> param = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.getType());
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).headers(headers).
                path("/greetingPost").enableExecutionTime(true).httpMethod(HttpMethod.POST).
                formParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_post_method_3() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON.getType());
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).headers(headers).
                path("/greetingPostJson").enableExecutionTime(true).httpMethod(HttpMethod.POST).
                body("{\n"
                    + "  \"id\": 1,\n"
                    +
                    "  \"content\": \"Hello John !!\"\n"
                    +
                    "}").build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_put_method() {
        Map<String, String> param = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).headers(headers).
                path("/greeting").enableExecutionTime(true).httpMethod(HttpMethod.PUT).
                formParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void uses_delete_method() {
        Map<String, String> param = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        param.put("name", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).headers(headers).
                path("/greeting").enableExecutionTime(true).httpMethod(HttpMethod.DELETE).
                formParams(param).build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }


}

package com.sms.satp.engine.model.impl;

import com.sms.satp.engine.model.ApiUnitResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.concurrent.TimeUnit;


public class DefaultApiUnitResponse implements ApiUnitResponse {

    private final Response response;


    public DefaultApiUnitResponse(Response response) {
        this.response = response;
    }


    @Override
    public String body() {
        return response.asString();

    }

    @Override
    public String cookie(String name) {
        return response.cookie(name);
    }

    @Override
    public int status() {
        return response.statusCode();
    }

    @Override
    public String statusLine() {
        return response.statusLine();
    }

    @Override
    public String header(String name) {
        return response.header(name);
    }

    @Override
    public JsonPath jsonPath() {
        return response.jsonPath();
    }

    @Override
    public long timeIn(TimeUnit timeUnit) {
        return response.timeIn(timeUnit);
    }


}

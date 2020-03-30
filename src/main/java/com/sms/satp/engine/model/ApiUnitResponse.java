package com.sms.satp.engine.model;

import io.restassured.path.json.JsonPath;
import java.util.concurrent.TimeUnit;


public interface ApiUnitResponse {

    public String body();

    public String cookie(String name);

    public int status();

    public String statusLine();

    public String header(String name);

    public JsonPath jsonPath();

    public long timeIn(TimeUnit timeUnit);


}

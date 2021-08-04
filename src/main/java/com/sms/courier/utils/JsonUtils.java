package com.sms.courier.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtils {

    private JsonUtils() {
    }

    static ObjectMapper mapper = new ObjectMapper();

    public static String serializeObject(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }
}
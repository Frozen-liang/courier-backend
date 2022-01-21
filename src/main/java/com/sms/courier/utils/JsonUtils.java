package com.sms.courier.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtils {

    private JsonUtils() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final ObjectMapper OBJECT_MAPPER_NOT_NULL = new ObjectMapper();

    public static String serializeObject(Object o) throws IOException {
        return MAPPER.writeValueAsString(o);
    }

    public static String serializeObjectNotNull(Object o) {
        try {
            OBJECT_MAPPER_NOT_NULL.setSerializationInclusion(Include.NON_NULL);
            return OBJECT_MAPPER_NOT_NULL.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

}
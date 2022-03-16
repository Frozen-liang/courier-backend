package com.sms.courier.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
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
            if (Objects.nonNull(o)) {
                OBJECT_MAPPER_NOT_NULL.setSerializationInclusion(Include.NON_NULL);
                return OBJECT_MAPPER_NOT_NULL.writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            log.error("Serialize object error!", e);
        }
        return "";
    }

    public static <T> T readValue(String json, Class<T> type) {
        try {
            if (StringUtils.isBlank(json)) {
                return null;
            }
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Pares json error!", e);
            return null;
        }
    }
}
package com.sms.satp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {

    public static String asJsonString(ObjectMapper objectMapper, final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

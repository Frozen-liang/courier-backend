package com.sms.courier.chat.common;

import java.util.Map;
import java.util.Objects;

public enum AdditionalParam {
    EMAIL_ATTACHMENT("attachment"),
    EMAIL_INLINES("inlines"),
    EMAIL_TO("to"),
    EMAIL_CC("cc");

    private final String key;

    AdditionalParam(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static <T> T getParamValueOrDefault(Map<AdditionalParam, Object> additionalParam,
        AdditionalParam param, Class<T> clazz, T defaultValue) {
        Object o = additionalParam.getOrDefault(param, defaultValue);
        if (Objects.nonNull(o)) {
            return clazz.cast(o);
        } else {
            return null;
        }
    }
}
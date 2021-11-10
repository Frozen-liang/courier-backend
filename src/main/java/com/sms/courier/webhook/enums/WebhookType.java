package com.sms.courier.webhook.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum WebhookType implements EnumCommon {
    WORKSPACE(0),
    PROJECT(1),
    TEST_CASE(2),
    API(3),
    SCENE_CASE(4),
    TEST_CASE_REPORT(5),
    SCENE_CASE_REPORT(6),
    JOB(7);


    private final Integer code;
    private static final Map<Integer, WebhookType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(WebhookType::getCode, Function.identity()));

    WebhookType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static WebhookType getType(Integer code) {
        return MAPPINGS.get(code);
    }
}

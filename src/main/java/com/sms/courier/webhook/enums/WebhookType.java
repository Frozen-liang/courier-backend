package com.sms.courier.webhook.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum WebhookType implements EnumCommon {
    API_ADD(0),
    API_UPDATE(1),
    API_DELETE(2),
    CASE_REPORT(3),
    SCENE_CASE_REPORT(4);

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

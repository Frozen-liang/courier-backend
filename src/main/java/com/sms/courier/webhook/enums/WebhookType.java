package com.sms.courier.webhook.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum WebhookType implements EnumCommon {
    CASE_REPORT(0, "CaseReport"),
    SCENE_CASE_REPORT(1, "SceneCaseReport"),
    SCHEDULE_END(2, "SchedulerEnd"),
    SCHEDULE_START(3, "SchedulerStart");

    private final Integer code;
    private final String name;
    private static final Map<Integer, WebhookType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(WebhookType::getCode, Function.identity()));

    WebhookType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getName() {
        return name;
    }

    public static WebhookType getType(Integer code) {
        return MAPPINGS.get(code);
    }
}

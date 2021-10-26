package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum CaseType implements EnumCommon {

    CASE(0, "caseCount"),
    SCENE_CASE(1, "sceneCaseCount"),
    OTHER_OBJECT_SCENE_CASE_COUNT(2, "otherProjectSceneCaseCount");

    CaseType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, CaseType> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(CaseType::getCode, Function.identity()));
    private final int code;
    private final String name;

    public String getName() {
        return name;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Nullable
    public static CaseType getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }


}

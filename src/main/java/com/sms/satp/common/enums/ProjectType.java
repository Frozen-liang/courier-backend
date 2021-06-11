package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum ProjectType implements EnumCommon {

    WEB(0),
    APP(1);
    private static final Map<Integer, ProjectType> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(ProjectType::getCode, Function.identity()));
    private final int code;

    ProjectType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ProjectType getType(@Nullable Integer code) {
        return Objects.isNull(code) ? null : MAPPINGS.get(code);
    }
}

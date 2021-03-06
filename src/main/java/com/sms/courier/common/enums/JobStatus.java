package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum JobStatus implements EnumCommon {
    RUNNING(0),
    SUCCESS(1),
    FAIL(2),
    PENDING(-1);
    private final int code;
    private static final Map<Integer, JobStatus> MAPPINGS =
        Arrays.stream(values()).sequential().collect(Collectors.toMap(JobStatus::getCode, Function.identity()));

    JobStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static JobStatus getType(@Nullable Integer code) {
        return Objects.nonNull(code) ? MAPPINGS.get(code) : null;
    }
}
